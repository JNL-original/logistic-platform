package com.jnl.courierservice.controller;

import com.jnl.courierservice.dto.LocationUpdateRequest;
import com.jnl.courierservice.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couriers")
public class DeliveryController {
    private final DeliveryService service;
    public DeliveryController(DeliveryService service){
        this.service = service;
    }
    @PostMapping("/online")
    public ResponseEntity<Void> goOnline(@RequestHeader("X-Courier-Id") Long courierId) {
        service.processOnline(courierId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offline")
    public ResponseEntity<Void> goOffline(@RequestHeader("X-Courier-Id") Long courierId) {
        service.processOffline(courierId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/geo")
    public ResponseEntity<Void> updateLocation(
            @RequestHeader("X-Courier-Id") Long courierId,
            @RequestBody LocationUpdateRequest request) {

        service.saveLocation(courierId, request);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> changeOrderStatus(
            @RequestHeader("X-Courier-Id") Long courierId,
            @PathVariable Long orderId,
            @RequestParam String status) {

        service.changeOrderStatus(courierId, orderId, status);
        return ResponseEntity.ok().build();
    }
}
