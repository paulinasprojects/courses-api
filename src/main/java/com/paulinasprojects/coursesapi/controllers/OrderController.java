package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.ErrorDto;
import com.paulinasprojects.coursesapi.dtos.OrderDto;
import com.paulinasprojects.coursesapi.exceptions.OrderNotFoundException;
import com.paulinasprojects.coursesapi.mappers.OrderMapper;
import com.paulinasprojects.coursesapi.repositories.OrderRepository;
import com.paulinasprojects.coursesapi.services.AuthService;
import com.paulinasprojects.coursesapi.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
 private final OrderService orderService;

  @GetMapping
  public List<OrderDto> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{orderId}")
  public OrderDto getOrder(@PathVariable(name = "orderId") Long orderId) {
    return orderService.getOrder(orderId);
  }
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Void> handleOrderNotFound() {
    return ResponseEntity.notFound().build();
  }
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handleAccessDenied(Exception ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
  }

}
