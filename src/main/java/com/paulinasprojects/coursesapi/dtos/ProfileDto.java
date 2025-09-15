package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
  private Long id;
  private String bio;
  private String phoneNumber;
  private LocalDate dateOfBirth;
  private Integer loyaltyPoints;
}
