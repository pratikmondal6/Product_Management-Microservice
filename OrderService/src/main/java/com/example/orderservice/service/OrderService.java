package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderLineItems;
import com.example.orderservice.model.OrderLineItemsModel;
import com.example.orderservice.model.OrderRequestModel;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;


    public void placeOrder(OrderRequestModel orderRequestModel){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems =
                orderRequestModel.getOrderLinemsModelList()
                .stream()
                .map((OrderLineItemsModel model) -> mapToDto(model))
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);

    }

    private OrderLineItems mapToDto(OrderLineItemsModel model){

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(model.getPrice());
        orderLineItems.setQuantity(model.getQuantity());
        orderLineItems.setSkuCode(model.getSkuCode());
        return orderLineItems;

    }
}
