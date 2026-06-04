package com.jnl.orderservice.mapper;

import com.jnl.orderservice.dto.OrderCreateRequest;
import com.jnl.orderservice.dto.OrderResponse;
import com.jnl.orderservice.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    Order toEntity(OrderCreateRequest request);

    List<OrderResponse> toResponseList(List<Order> orders);
}