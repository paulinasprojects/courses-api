package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CourseDto;
import com.paulinasprojects.coursesapi.exceptions.CourseNotFoundException;
import com.paulinasprojects.coursesapi.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
  private final CourseService courseService;

  @PostMapping
  public ResponseEntity<CourseDto> createCourse(
          @RequestBody CourseDto courseDto,
          UriComponentsBuilder uriBuilder
  ) {
   var savedCourseDto = courseService.createCourse(courseDto);

    var uri = uriBuilder.path("/courses/{id}").buildAndExpand(savedCourseDto.getId()).toUri();

    return ResponseEntity.created(uri).body(savedCourseDto);
  }

  @GetMapping("/{id}")
  public CourseDto getCourse(@PathVariable(name = "id") Long id
  ) {
    return courseService.getCourse(id);
  }

  @GetMapping
  public List<CourseDto> getAllCourses(@RequestParam(name = "categoryId", required = false) Byte categoryId
  ) {
      return courseService.getAllCourses(categoryId);
  }

  @PutMapping("/{id}")
  public CourseDto updateCourse(
          @PathVariable(name = "id") Long id,
          @RequestBody  CourseDto courseDto
  ) {
    return courseService.updateCourse(id, courseDto);
  }

  @DeleteMapping("/{id}")
  public void deleteCourse(@PathVariable(name = "id") Long id
  ) {
    courseService.deleteCourse(id);
  }

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<Void> handleCourseNotFound() {
    return ResponseEntity.notFound().build();
  }
}
