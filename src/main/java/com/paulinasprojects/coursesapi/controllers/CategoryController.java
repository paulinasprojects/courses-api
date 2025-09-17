package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CategoryDto;
import com.paulinasprojects.coursesapi.dtos.UpdateCategoryReq;
import com.paulinasprojects.coursesapi.exceptions.CategoryNotFoundException;
import com.paulinasprojects.coursesapi.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<CategoryDto> createCategory(
        @Valid @RequestBody CategoryDto categoryDto,
          UriComponentsBuilder uriBuilder
  ) {
   var saved = categoryService.createCategory(categoryDto);

    var uri = uriBuilder.path("/categories/{id}").buildAndExpand(saved.getId()).toUri();
    return ResponseEntity.created(uri).body(saved);
  }

  @GetMapping("/{id}")
  public CategoryDto getCategory(@PathVariable Byte id
  ) {
   return categoryService.getCategory(id);
  }

  @GetMapping
  public Iterable<CategoryDto> getAllCategories(
     @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
  ) {
   return categoryService.getAllCategories(sortBy);
  }

  @PutMapping("/{id}")
  public CategoryDto updateCategory(
          @PathVariable(name = "id") Byte id,
          @RequestBody UpdateCategoryReq req
  ) {
   return categoryService.updateCategory(id, req);
  }

  @DeleteMapping("/{id}")
  public void deleteCategory(@PathVariable(name = "id") Byte id
  ) {
    categoryService.deleteCategory(id);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<Void> handleCategoryNotFound() {
    return ResponseEntity.notFound().build();
  }
}
