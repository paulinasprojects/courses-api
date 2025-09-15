package com.paulinasprojects.coursesapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileReq {
  private String bio;
  private String phoneNumber;
  private LocalDate dateOfBirth;
  private Integer loyaltyPoints;
}
