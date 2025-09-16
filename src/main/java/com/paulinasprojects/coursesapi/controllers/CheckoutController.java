package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CheckoutReq;
import com.paulinasprojects.coursesapi.dtos.CheckoutResponse;
import com.paulinasprojects.coursesapi.dtos.ErrorDto;
import com.paulinasprojects.coursesapi.exceptions.CartEmptyException;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.PaymentException;
import com.paulinasprojects.coursesapi.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping
  public CheckoutResponse checkout(
     @Valid @RequestBody CheckoutReq request
  ) {
      return checkoutService.checkout(request);
    }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<?> handlePaymentException() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Error creating a checkout session"));
  }

  @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
    return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
  }
}
