package com.jnl.courierservice.service;
import com.jnl.courierservice.dto.CourierCreateRequest;
import com.jnl.courierservice.dto.CourierResponse;
import com.jnl.courierservice.exception.ResourceNotFoundException;
import com.jnl.courierservice.mapper.CourierMapper;
import com.jnl.courierservice.model.Courier;
import com.jnl.courierservice.model.CourierStatus;
import com.jnl.courierservice.repository.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CourierService {
    private final CourierRepository repo;
    private final CourierMapper mapper;
    public CourierService(CourierRepository repo, CourierMapper mapper){
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CourierResponse getCourierById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Курьер с id  " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<CourierResponse> getCouriers(String status) {
        if(!StringUtils.hasText(status))
            return mapper.toResponseList(repo.findAll());
        try {
            CourierStatus validStatus = CourierStatus.valueOf(status.toUpperCase().trim());
            List<Courier> filteredOrders = repo.findAllByStatus(validStatus);
            return mapper.toResponseList(filteredOrders);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Несуществующий статус курьера: " + status);
        }
    }

    @Transactional
    public CourierResponse register(CourierCreateRequest request) {
        Courier newCourier = mapper.toEntity(request);
        newCourier.setStatus(CourierStatus.AVAILABLE);
        Courier saved = repo.save(newCourier);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        try {
            CourierStatus validStatus = CourierStatus.valueOf(status.toUpperCase().trim());
            Courier courier = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Курьер с id " + id + " не найден"));

            courier.setStatus(validStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Несуществующий статус курьера: " + status);
        }
    }

    @Transactional
    public CourierResponse updateCourier(Long id, CourierCreateRequest request) {
        Courier existingCourier = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Курьер с id " + id + " не найден"));

        mapper.updateEntityFromDto(request, existingCourier);

        Courier updated = repo.save(existingCourier);
        return mapper.toResponse(updated);
    }
}
