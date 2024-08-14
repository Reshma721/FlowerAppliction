package com.floral.floralfiessy.repository;

import com.floral.floralfiessy.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
