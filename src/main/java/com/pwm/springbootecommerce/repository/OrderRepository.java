package com.pwm.springbootecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pwm.springbootecommerce.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
