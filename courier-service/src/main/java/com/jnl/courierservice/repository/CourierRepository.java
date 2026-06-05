package com.jnl.courierservice.repository;

import com.jnl.courierservice.model.Courier;
import com.jnl.courierservice.model.CourierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findAllByStatus(CourierStatus validStatus);
}