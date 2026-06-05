package com.jnl.courierservice.grpc;

import com.jnl.orderservice.grpc.ActiveOrdersRequest;
import com.jnl.orderservice.grpc.ActiveOrdersResponse;
import com.jnl.orderservice.grpc.OrderGrpcServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderGrpcClient {

    @GrpcClient("order-service")
    private OrderGrpcServiceGrpc.OrderGrpcServiceBlockingStub orderStub;

    public List<Long> getActiveOrderIdsByCourier(Long courierId) {
        ActiveOrdersRequest request = ActiveOrdersRequest.newBuilder()
                .setCourierId(courierId)
                .build();

        ActiveOrdersResponse response = orderStub.getActiveOrdersByCourier(request);

        return response.getOrderIdsList();
    }
}