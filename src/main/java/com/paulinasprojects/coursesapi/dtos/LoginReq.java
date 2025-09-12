package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
  @NotBlank(message = "Email is required")
  @Email
  private String email;

  @NotBlank(message = "Password is required")
  private String password;
}
