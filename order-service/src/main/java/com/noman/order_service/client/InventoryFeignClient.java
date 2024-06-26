package com.noman.order_service.client;

import com.noman.order_service.dto.InventoryStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "INVENTORY-SERVICE", path = "/api/inventory/")
public interface InventoryFeignClient {

    @RequestMapping(value = "/{sku-code}", method = RequestMethod.GET)
    boolean isInStock(@PathVariable("sku-code") String skuCode);

    @RequestMapping(method = RequestMethod.GET)
    List<InventoryStockResponse> checkAllInStock(@RequestParam List<String> skuCodes);
}
