package com.jnl.courierservice.mapper;

import com.jnl.courierservice.dto.CourierCreateRequest;
import com.jnl.courierservice.dto.CourierResponse;
import com.jnl.courierservice.model.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    CourierResponse toResponse(Courier order);

    Courier toEntity(CourierCreateRequest request);

    List<CourierResponse> toResponseList(List<Courier> orders);

    void updateEntityFromDto(CourierCreateRequest dto, @MappingTarget Courier entity);
}