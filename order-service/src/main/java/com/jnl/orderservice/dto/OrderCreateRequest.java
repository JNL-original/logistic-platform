package com.jnl.orderservice.dto;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private Double senderLat;
    private Double senderLng;
    private Double deliveryLat;
    private Double deliveryLng;
    private String comment;
    private Double weight;
    private Double volume;
}