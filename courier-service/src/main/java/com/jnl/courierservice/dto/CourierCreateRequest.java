package com.jnl.courierservice.dto;

import lombok.Data;

@Data
public class CourierCreateRequest {
    private String name;
    private String email;
}