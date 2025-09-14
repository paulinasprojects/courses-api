package com.paulinasprojects.coursesapi.exceptions;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException() {
    super("Cart not found");
  }
}
