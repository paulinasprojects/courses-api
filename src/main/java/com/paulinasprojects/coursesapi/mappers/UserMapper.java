package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.UserDto;
import com.paulinasprojects.coursesapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
}
