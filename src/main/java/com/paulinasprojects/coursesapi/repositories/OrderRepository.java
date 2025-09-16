package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}