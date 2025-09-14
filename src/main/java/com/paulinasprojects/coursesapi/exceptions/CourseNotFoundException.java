package com.paulinasprojects.coursesapi.exceptions;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Course not found");
  }
}
