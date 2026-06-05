package com.jnl.courierservice.service;

import com.jnl.courierservice.dto.LocationUpdateRequest;
import com.jnl.courierservice.exception.CourierBannedException;
import com.jnl.courierservice.exception.ResourceNotFoundException;
import com.jnl.courierservice.grpc.OrderGrpcClient;
import com.jnl.courierservice.model.Courier;
import com.jnl.courierservice.model.CourierStatus;
import com.jnl.courierservice.repository.CourierRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryService {
    private final CourierRepository repo;
    private final StringRedisTemplate redisTemplate;
    private final OrderGrpcClient orderGrpcClient;
    private static final String HASH_PREFIX = "courier:active:";
    private static final String GEO_KEY = "couriers:locations";

    public DeliveryService(CourierRepository repo, StringRedisTemplate redisTemplate, OrderGrpcClient orderGrpcClient){
        this.repo = repo;
        this.redisTemplate = redisTemplate;
        this.orderGrpcClient = orderGrpcClient;
    }

    public void processOnline(Long courierId) {
        Courier courier = repo.findById(courierId)
                .orElseThrow(() -> new ResourceNotFoundException("Курьер с id  " + courierId + " не найден"));;
        if(courier.getStatus().equals(CourierStatus.BANNED))
            throw new CourierBannedException("Ваш аккаунт заблокирован");
        if(courier.getStatus().equals(CourierStatus.INACTIVE))
            repo.updateStatus(courierId, CourierStatus.AVAILABLE);
        String courierKey = HASH_PREFIX + courierId;
        String ordersSetKey = "courier:orders:" + courierId;

        List<Long> activeOrders = orderGrpcClient.getActiveOrderIdsByCourier(courierId);

        if (activeOrders != null && !activeOrders.isEmpty()) {
            redisTemplate.opsForHash().put(courierKey, "status", "BUSY");

            redisTemplate.delete(ordersSetKey);

            String[] orderIdsArray = activeOrders.stream()
                    .map(Object::toString)
                    .toArray(String[]::new);

            redisTemplate.opsForSet().add(ordersSetKey, orderIdsArray);
        } else {
            redisTemplate.opsForHash().put(courierKey, "status", "FREE");
            redisTemplate.delete(ordersSetKey);
        }
    }

    public void processOffline(Long courierId) {
        String key = HASH_PREFIX + courierId;

        String status = (String) redisTemplate.opsForHash().get(key, "status");
        if ("BUSY".equals(status)) {
            throw new IllegalStateException("Нельзя выйти из сети, пока вы на заказе!");
        }

        redisTemplate.delete(key);

        redisTemplate.opsForGeo().remove(GEO_KEY, courierId.toString());
    }

    public void saveLocation(Long courierId, LocationUpdateRequest request) {
        String key = HASH_PREFIX + courierId;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new IllegalStateException("Курьер должен быть в сети (online) для обновления гео-позиции");
        }

        Point point = new Point(request.getLng(), request.getLat());
        redisTemplate.opsForGeo().add(GEO_KEY, point, courierId.toString());
    }

    public void changeOrderStatus(Long courierId, Long orderId, String status) {
        // На данном этапе мы просто принимаем запрос от курьера.
        // Здесь в будущем будет отправка события в Kafka топик "courier-order-updates".
        // Прямо сейчас менять статус в Redis здесь не нужно — это сделает слушатель Kafka,
        // когда сервис заказов (order-service) подтвердит операцию.
        //todo
    }
}
