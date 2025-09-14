package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartReq {
  @NotNull
  private Long courseId;
}
