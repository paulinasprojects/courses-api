package com.paulinasprojects.coursesapi.exceptions;

public class CartEmptyException extends RuntimeException {
  public  CartEmptyException() {
    super("Cart is empty");
  }
}
