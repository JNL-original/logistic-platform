package com.jnl.orderservice.controller;

import com.jnl.orderservice.dto.OrderCreateRequest;
import com.jnl.orderservice.dto.OrderResponse;
import com.jnl.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){
        this.service = service;
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = service.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam(required = false) String status) {
        List<OrderResponse> orders = service.getOrders(status);
        return ResponseEntity.ok(orders);
    }
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        OrderResponse order = service.saveOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        service.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
