package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.UpdateUserReq;
import com.paulinasprojects.coursesapi.dtos.UserDto;
import com.paulinasprojects.coursesapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  void updateUser(UpdateUserReq request, @MappingTarget User user);
}
