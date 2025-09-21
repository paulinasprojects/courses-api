package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.CheckoutReq;
import com.paulinasprojects.coursesapi.dtos.CheckoutResponse;
import com.paulinasprojects.coursesapi.dtos.ErrorDto;
import com.paulinasprojects.coursesapi.entities.OrderStatus;
import com.paulinasprojects.coursesapi.exceptions.CartEmptyException;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.PaymentException;
import com.paulinasprojects.coursesapi.repositories.OrderRepository;
import com.paulinasprojects.coursesapi.services.CheckoutService;
import com.paulinasprojects.coursesapi.services.WebhookReq;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;
  private final OrderRepository orderRepository;


  @PostMapping
  public CheckoutResponse checkout(
     @Valid @RequestBody CheckoutReq request
  ) {
      return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
      public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookReq(headers, payload));
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
