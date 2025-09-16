package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
  private OrderCourseDto course;
  private int quantity;
  private BigDecimal totalPrice;
}
