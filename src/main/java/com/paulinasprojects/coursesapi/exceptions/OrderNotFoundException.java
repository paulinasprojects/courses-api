package com.paulinasprojects.coursesapi.exceptions;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException() {
    super("Order not found");
  }
}
