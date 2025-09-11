package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.entities.Category;
import com.paulinasprojects.coursesapi.mappers.CategoryMapper;
import com.paulinasprojects.coursesapi.repositories.CategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @PostMapping
  public ResponseEntity<CategoryDto> createCategory(
        @Valid @RequestBody CategoryDto categoryDto,
          UriComponentsBuilder uriBuilder
  ) {
    Category category = categoryMapper.toEntity(categoryDto);
   Category saved =  categoryRepository.save(category);

    var uri = uriBuilder.path("/categories/{id}").buildAndExpand(saved.getId()).toUri();
    return ResponseEntity.created(uri).body(categoryMapper.toDto(saved));
  }
}
