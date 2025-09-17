package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.dtos.UpdateCategoryReq;
import com.paulinasprojects.coursesapi.exceptions.CategoryNotFoundException;
import com.paulinasprojects.coursesapi.mappers.CategoryMapper;
import com.paulinasprojects.coursesapi.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CategoryDto createCategory(CategoryDto categoryDto) {
    var category = categoryMapper.toEntity(categoryDto);
    var savedCategory =  categoryRepository.save(category);
    return categoryMapper.toDto(savedCategory);
  }

  public CategoryDto getCategory(Byte categoryId) {
    var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    return categoryMapper.toDto(category);
  }

  public Iterable<CategoryDto> getAllCategories(String sortBy) {
    if (!Objects.equals("name", sortBy)) {
      sortBy = "name";
    }

    return  categoryRepository.findAll(Sort.by(sortBy))
            .stream()
            .map(categoryMapper::toDto)
            .toList();
  }

  public CategoryDto updateCategory(Byte categoryId, UpdateCategoryReq req) {
    var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    categoryMapper.updateCategory(req, category);
    categoryRepository.save(category);
    return categoryMapper.toDto(category);
  }

  public void deleteCategory(Byte categoryId) {
    var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    categoryRepository.delete(category);
  }
}
