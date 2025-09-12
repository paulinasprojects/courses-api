package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
