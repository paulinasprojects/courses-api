package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);
  Category toEntity(CategoryDto categoryDto);
}
