package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.CourseDto;
import com.paulinasprojects.coursesapi.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
  @Mapping(target = "categoryId", source = "category.id")
  CourseDto toDto(Course course);
  Course toEntity(CourseDto courseDto);
}
