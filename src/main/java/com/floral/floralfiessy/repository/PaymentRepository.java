package com.floral.floralfiessy.repository;

import com.floral.floralfiessy.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
