package com.paulinasprojects.coursesapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
  private Long id;
  private String name;
  private String email;
}
