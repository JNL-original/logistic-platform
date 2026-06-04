package com.jnl.orderservice.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private Double senderLat;
    private Double senderLng;
    private Double deliveryLat;
    private Double deliveryLng;
    private String comment;
    private Double weight;
    private Double volume;
    private String status;
    private Long courierId;
}
