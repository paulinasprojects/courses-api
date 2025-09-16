package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.OrderDto;
import com.paulinasprojects.coursesapi.exceptions.OrderNotFoundException;
import com.paulinasprojects.coursesapi.mappers.OrderMapper;
import com.paulinasprojects.coursesapi.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
  private final AuthService authService;
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public List<OrderDto> getAllOrders() {
    var user =  authService.getCurrentUser();
    var orders = orderRepository.getOrdersByCustomer(user);
    return orders.stream().map(orderMapper::toOrderDto).toList();
  }

  public OrderDto getOrder(Long orderId) {
    var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);
    var user = authService.getCurrentUser();
    if (!order.isPlacedBy(user)) {
      throw new AccessDeniedException("You don't have access to this order");
    }
    return orderMapper.toOrderDto(order);
  }
}
