package com.noman.order_service.service;

import com.noman.order_service.client.InventoryFeignClient;
import com.noman.order_service.dto.InventoryStockResponse;
import com.noman.order_service.dto.OrderLineItemDto;
import com.noman.order_service.dto.OrderRequest;
import com.noman.order_service.event.OrderPlacedEvent;
import com.noman.order_service.model.Order;
import com.noman.order_service.model.OrderLineItem;
import com.noman.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryFeignClient inventoryFeignClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    // "inventory" is the same as for what we set the properties for
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {
        var skuList= orderRequest.getOrderLineItemDtoList().stream().map(OrderLineItemDto::getSkuId).toList();
        var inventoryResponse = inventoryFeignClient.checkAllInStock(skuList);
        boolean allProductsInStock = inventoryResponse.stream().allMatch(InventoryStockResponse::isInStock);
        var productsNotInStock = inventoryResponse.stream().filter(inventoryResponse1 -> !inventoryResponse1.isInStock()).map(InventoryStockResponse::getSkuCode).toArray();

        if (allProductsInStock) {
            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderLineItems(orderRequest.getOrderLineItemDtoList().stream().map(orderLineItemDto -> mapOrderLineItem(orderLineItemDto, order)).toList());
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderId()));
            return CompletableFuture.supplyAsync(() -> "Order Placed successfully.");
        } else {
            throw new IllegalArgumentException("Product is not in stock please try again later. " + Arrays.toString(productsNotInStock));
        }
    }

    // return type must be same as placeOrder
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong try later.") ;
    }

    private OrderLineItem mapOrderLineItem(OrderLineItemDto orderLineItemDto, Order order) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrder(order);
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setSkuId(orderLineItemDto.getSkuId());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        return orderLineItem;

    }

}
