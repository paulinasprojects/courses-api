package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
  @EntityGraph(attributePaths = "category")
  List<Course> findByCategoryId(Byte categoryId);

  @EntityGraph(attributePaths = "category")
  @Query("SELECT c FROM Course c")
  List<Course> findAllWithCategory();
}
