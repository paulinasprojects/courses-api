package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

@Data
public class AddressDto {
  private Long id;
  private String street;
  private String city;
  private String state;
  private String zip;
}
