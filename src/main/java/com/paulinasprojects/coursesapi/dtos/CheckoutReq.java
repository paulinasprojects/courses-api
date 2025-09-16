package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutReq {
  @NotNull(message = "Cart id is required")
  private UUID cartId;
}
