package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemReq {
  @NotNull(message = "Quantity must be provided")
  @Min(value = 1, message = "Quantity must be greater than 0")
  @Max(value = 1000, message = "Quantity must be lest than 1000")
  private Integer quantity;
}
