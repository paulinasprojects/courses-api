package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.dtos.UpdateCategoryReq;
import com.paulinasprojects.coursesapi.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category category);
  Category toEntity(CategoryDto categoryDto);

  @Mapping(target = "name", source = "name")
  void updateCategory(UpdateCategoryReq request, @MappingTarget Category category);
}
