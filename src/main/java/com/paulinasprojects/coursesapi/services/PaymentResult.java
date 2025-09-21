package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
  private Long orderId;
  private OrderStatus orderStatus;
}
