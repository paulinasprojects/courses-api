package com.paulinasprojects.coursesapi.dtos;

import lombok.Data;

@Data
public class ChangePasswordReq {
  private String oldPassword;
  private String newPassword;
}
