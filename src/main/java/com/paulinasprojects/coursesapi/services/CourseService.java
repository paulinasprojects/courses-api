package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.CourseDto;
import com.paulinasprojects.coursesapi.entities.Category;
import com.paulinasprojects.coursesapi.entities.Course;
import com.paulinasprojects.coursesapi.exceptions.CategoryNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.CourseNotFoundException;
import com.paulinasprojects.coursesapi.mappers.CourseMapper;
import com.paulinasprojects.coursesapi.repositories.CategoryRepository;
import com.paulinasprojects.coursesapi.repositories.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final CategoryRepository categoryRepository;

  public CourseDto createCourse(CourseDto courseDto) {
    var category = findCategoryById(courseDto);
    var course = courseMapper.toEntity(courseDto);
    course.setCategory(category);
    courseRepository.save(course);
    courseDto.setId(course.getId());

    return courseMapper.toDto(course);
  }

  public CourseDto getCourse(Long courseId) {
    var course = findCourseById(courseId);
    return courseMapper.toDto(course);
  }

  public List<CourseDto> getAllCourses(Byte categoryId) {
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

  public CourseDto updateCourse(Long courseId, CourseDto courseDto) {
    var category = findCategoryById(courseDto);
    var course = findCourseById(courseId);
    courseMapper.updateCourse(courseDto, course);
    course.setCategory(category);
    courseRepository.save(course);
    courseDto.setId(course.getId());
    return courseMapper.toDto(course);
  }

  public void deleteCourse(Long courseId) {
    var course = findCourseById(courseId);
    courseRepository.delete(course);
  }

  private Category findCategoryById(CourseDto courseDto) {
    return categoryRepository.findById(courseDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
  }

  private Course findCourseById(Long courseId) {
    return courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
  }

}
