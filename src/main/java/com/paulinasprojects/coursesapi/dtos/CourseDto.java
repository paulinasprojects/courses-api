package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDto {
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Byte categoryId;
}
