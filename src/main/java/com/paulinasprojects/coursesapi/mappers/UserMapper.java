package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  User toEntity(RegisterUserReq request);
  void updateUser(UpdateUserReq request, @MappingTarget User user);
}
