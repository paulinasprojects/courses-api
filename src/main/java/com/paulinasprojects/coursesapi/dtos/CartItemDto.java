package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
  private CartCourseDto course;
  private int quantity;
  private BigDecimal totalPrice;
}
