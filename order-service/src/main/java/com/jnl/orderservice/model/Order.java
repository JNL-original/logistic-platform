package com.jnl.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_lat", nullable = false)
    private Double senderLat;

    @Column(name = "sender_lng", nullable = false)
    private Double senderLng;

    @Column(name = "delivery_lat", nullable = false)
    private Double deliveryLat;

    @Column(name = "delivery_lng", nullable = false)
    private Double deliveryLng;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @Column(name = "courier_id")
    private Long courierId;
}