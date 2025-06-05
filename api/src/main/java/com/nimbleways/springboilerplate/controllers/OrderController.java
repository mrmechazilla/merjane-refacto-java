package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;

import com.nimbleways.springboilerplate.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/v1/orders", "/orders"})
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping({"{orderId}/process", "{orderId}/processOrder"})
    public ResponseEntity<ProcessOrderResponse> processOrder(@PathVariable Long orderId) {
        ProcessOrderResponse response = orderService.processOrder(orderId);
        return ResponseEntity.ok(response);
    }
}
