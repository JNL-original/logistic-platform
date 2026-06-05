package com.jnl.orderservice.grpc;

import com.jnl.orderservice.model.Order;
import com.jnl.orderservice.model.OrderStatus;
import com.jnl.orderservice.repository.OrderRepository;
import com.jnl.orderservice.grpc.ActiveOrdersRequest;
import com.jnl.orderservice.grpc.ActiveOrdersResponse;
import com.jnl.orderservice.grpc.OrderGrpcServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class OrderGrpcServiceImpl extends OrderGrpcServiceGrpc.OrderGrpcServiceImplBase {

    private final OrderRepository orderRepository;

    public OrderGrpcServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void getActiveOrdersByCourier(ActiveOrdersRequest request,
                                         StreamObserver<ActiveOrdersResponse> responseObserver) {

        Long courierId = request.getCourierId();

        List<Order> activeOrders = orderRepository.findAllByCourierIdAndStatus(courierId, OrderStatus.DELIVERING);

        List<Long> orderIds = activeOrders.stream()
                .map(Order::getId)
                .toList();

        ActiveOrdersResponse response = ActiveOrdersResponse.newBuilder()
                .addAllOrderIds(orderIds)
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}