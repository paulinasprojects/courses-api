package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CourseDto;
import com.paulinasprojects.coursesapi.entities.Course;
import com.paulinasprojects.coursesapi.mappers.CourseMapper;
import com.paulinasprojects.coursesapi.repositories.CategoryRepository;
import com.paulinasprojects.coursesapi.repositories.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final CategoryRepository categoryRepository;

  @PostMapping
  public ResponseEntity<CourseDto> createCourse(
          @RequestBody CourseDto courseDto,
          UriComponentsBuilder uriBuilder
  ) {
    var category = categoryRepository.findById(courseDto.getCategoryId()).orElse(null);
    if (category == null) {
      return ResponseEntity.badRequest().build();
    }
    var course = courseMapper.toEntity(courseDto);
    course.setCategory(category);
    courseRepository.save(course);
    courseDto.setId(course.getId());

    var uri = uriBuilder.path("/courses/{id}").buildAndExpand(courseDto.getId()).toUri();

    return ResponseEntity.created(uri).body(courseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourse(
          @PathVariable(name = "id") Long id
  ) {
    var course = courseRepository.findById(id).orElse(null);
    if (course == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(courseMapper.toDto(course));
  }

  @GetMapping
  public List<CourseDto> getAllCourses(
          @RequestParam(name = "categoryId", required = false) Byte categoryId
  ) {
    List<Course> courses;
    if (categoryId != null) {
      courses = courseRepository.findByCategoryId(categoryId);
    } else {
      courses = courseRepository.findAllWithCategory();
    }

    return  courses
            .stream()
            .map(courseMapper::toDto)
            .toList();
  }
}
