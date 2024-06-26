package com.noman.inventory_service.service;

import com.noman.inventory_service.dto.InventoryStockResponse;
import com.noman.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        var inventory = inventoryRepository.findBySkuCode(skuCode);
        if (inventory.isPresent() && inventory.get().getQuantity() > 0) {
            return true;
        }
        return false;
    }

    @SneakyThrows // Dont use it on production
    @Transactional(readOnly = true)
    public List<InventoryStockResponse> checkAllInStock(List<String> skuCodes) {
        var inventory = inventoryRepository.findBySkuCodeIn(skuCodes);
        return inventory.stream().map(inventoryResponse ->
            InventoryStockResponse.builder()
                    .skuCode(inventoryResponse.getSkuCode())
                    .isInStock(inventoryResponse.getQuantity() > 0)
                    .build()
        ).toList();
    }
}
