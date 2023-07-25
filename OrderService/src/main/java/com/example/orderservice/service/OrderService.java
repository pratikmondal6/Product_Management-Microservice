package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderLineItems;
import com.example.orderservice.model.InventoryResponse;
import com.example.orderservice.model.OrderLineItemsModel;
import com.example.orderservice.model.OrderRequestModel;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClient webClient;


    public void placeOrder(OrderRequestModel orderRequestModel){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems =
                orderRequestModel.getOrderLinemsModelList()
                .stream()
                .map((OrderLineItemsModel model) -> mapToDto(model))
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes =  order.getOrderLineItemsList().stream()
                .map(orderLineItem -> orderLineItem.getSkuCode()).toList();

        //check stock
        InventoryResponse[] inventoryResponseArray =  webClient.get()
                .uri("http://localhost:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();

        boolean allProductsInStock =
                Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

       if(allProductsInStock){
           orderRepository.save(order);
       }else {
           throw new IllegalArgumentException("Stock Finished, try later");

       }

    }

    private OrderLineItems mapToDto(OrderLineItemsModel model){

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(model.getPrice());
        orderLineItems.setQuantity(model.getQuantity());
        orderLineItems.setSkuCode(model.getSkuCode());
        return orderLineItems;

    }
}
