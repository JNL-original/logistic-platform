package com.jnl.orderservice.service;
import com.jnl.orderservice.dto.OrderCreateRequest;
import com.jnl.orderservice.dto.OrderResponse;
import com.jnl.orderservice.exception.ResourceNotFoundException;
import com.jnl.orderservice.mapper.OrderMapper;
import com.jnl.orderservice.model.Order;
import com.jnl.orderservice.model.OrderStatus;
import com.jnl.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;
    private final OrderMapper mapper;
    public OrderService(OrderRepository repo, OrderMapper mapper){
        this.repo = repo;
        this.mapper = mapper;
    }
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ " + id + " не найден"));
    }
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String statusStr) {
        if(!StringUtils.hasText(statusStr))
            return mapper.toResponseList(repo.findAll());
        try {
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase().trim());
            List<Order> filteredOrders = repo.findAllByStatus(status);
            return mapper.toResponseList(filteredOrders);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Несуществующий статус заказа: " + statusStr);
        }
    }
    @Transactional
    public OrderResponse saveOrder(OrderCreateRequest request) {
        Order newOrder = mapper.toEntity(request);
        newOrder.setStatus(OrderStatus.CREATED);
        Order saved = repo.save(newOrder);
        return mapper.toResponse(saved);
    }
    @Transactional
    public void cancelOrder(Long id) {
        int rowsUpdated = repo.updateOrderStatus(id, OrderStatus.CANCELLED);

        if (rowsUpdated == 0) {
            throw new ResourceNotFoundException("Заказ " + id + " не найден");
        }
    }
}
