package com.jnl.courierservice.dto;

import com.jnl.courierservice.model.CourierStatus;
import lombok.Data;

@Data
public class CourierResponse {
    private Long id;
    private String name;
    private String email;
    private CourierStatus status;
}
