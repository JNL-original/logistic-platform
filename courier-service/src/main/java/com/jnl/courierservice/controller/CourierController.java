package com.jnl.courierservice.controller;

import com.jnl.courierservice.dto.CourierCreateRequest;
import com.jnl.courierservice.dto.CourierResponse;
import com.jnl.courierservice.service.CourierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {
    private final CourierService service;
    public CourierController(CourierService service){
        this.service = service;
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourierResponse> getCourierById(@PathVariable Long id) {
        CourierResponse courier = service.getCourierById(id);
        return ResponseEntity.ok(courier);
    }
    @GetMapping
    public ResponseEntity<List<CourierResponse>> getCouriers(
            @RequestParam(required = false) String status) {
        List<CourierResponse> couriers = service.getCouriers(status);
        return ResponseEntity.ok(couriers);
    }
    @PostMapping
    public ResponseEntity<CourierResponse> registerCourier(@RequestBody CourierCreateRequest request) {
        CourierResponse courier = service.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(courier);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateCourierStatus(@PathVariable Long id, @RequestParam(required = false) String status) {
        service.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CourierResponse> updateCourier(@PathVariable Long id, @RequestBody CourierCreateRequest request) {
        CourierResponse courier = service.updateCourier(id, request);
        return ResponseEntity.ok(courier);
    }
}
