package com.example.inventoryservice.service;

import com.example.inventoryservice.model.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> skuCode){

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                  InventoryResponse.builder().skuCode(inventory.getSkuCode())
                          .isInStock(inventory.getQuantity() > 0 )
                          .build()
                ).toList();
    }
}