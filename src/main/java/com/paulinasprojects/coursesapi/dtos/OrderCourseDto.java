package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCourseDto {
  private Long id;
  private String name;
  private BigDecimal price;
}
