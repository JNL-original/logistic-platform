package com.jnl.courierservice.repository;

import com.jnl.courierservice.model.Courier;
import com.jnl.courierservice.model.CourierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findAllByStatus(CourierStatus validStatus);
    @Modifying
    @Transactional
    @Query("UPDATE Courier c SET c.status = :status WHERE c.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") CourierStatus status);
}