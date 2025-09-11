package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
  private Byte id;

  @NotBlank(message = "Category name is required")
  private String name;
}
