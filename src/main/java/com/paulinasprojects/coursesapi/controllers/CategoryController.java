package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.entities.Category;
import com.paulinasprojects.coursesapi.mappers.CategoryMapper;
import com.paulinasprojects.coursesapi.repositories.CategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategory(
    @PathVariable Byte id
  ) {
    var category = categoryRepository.findById(id).orElse(null);
    if (category == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(categoryMapper.toDto(category));
  }

  @GetMapping
  public Iterable<CategoryDto> getAllCategories(
          @RequestParam(required = false, defaultValue = "", name = "sort") String sort
  ) {
    if (!Objects.equals("name", sort)) {
      sort= "name";
    }

    return  categoryRepository.findAll(Sort.by(sort))
            .stream()
            .map(categoryMapper::toDto)
            .toList();
  }


}
