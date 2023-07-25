package com.example.orderservice.controller;

import com.example.orderservice.model.OrderRequestModel;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOder(@RequestBody OrderRequestModel orderRequestModel){
        orderService.placeOrder(orderRequestModel);
        return "order saved";
    }
}
