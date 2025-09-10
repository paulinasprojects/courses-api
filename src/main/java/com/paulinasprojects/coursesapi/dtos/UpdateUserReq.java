package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

@Data
public class UpdateUserReq {
  private String name;
  private String email;
}
