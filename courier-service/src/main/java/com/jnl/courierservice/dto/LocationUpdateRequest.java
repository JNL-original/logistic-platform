package com.jnl.courierservice.dto;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private Double lat;
    private Double lng;
}