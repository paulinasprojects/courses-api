package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.CourseNotFoundException;
import com.paulinasprojects.coursesapi.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
  private final CartService cartService;

  @PostMapping
  public ResponseEntity<CartDto> createCart(
          UriComponentsBuilder uriBuilder
  ) {
    var cartDto = cartService.createCart();
    var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

    return ResponseEntity.created(uri).body(cartDto);
  }

  @PostMapping("/{cartId}/items")
  public ResponseEntity<CartItemDto> addToCart(
          @PathVariable UUID cartId,
          @RequestBody AddItemToCartReq request
  ) {
      var cartItemDto = cartService.addToCart(cartId, request.getCourseId());
      return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
  }

  @GetMapping("/{cartId}")
  public CartDto getCart(@PathVariable UUID cartId) {
    return  cartService.getCart(cartId);
  }

  @PutMapping("/{cartId}/items/{courseId}")
  public CartItemDto updateItem(
          @PathVariable("cartId") UUID cartId,
          @PathVariable("courseId") Long courseId,
          @Valid @RequestBody UpdateCartItemReq request
          ) {
    return  cartService.updateItem(cartId, courseId, request.getQuantity());
  }

  @DeleteMapping("/{cartId}/items/{courseId}")
  public ResponseEntity<?> removeItem(
          @PathVariable("cartId") UUID cartId,
          @PathVariable("courseId") Long courseId
  ) {
    cartService.removeItem(cartId, courseId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cartId}/items")
  public ResponseEntity<Void> clearCart(
          @PathVariable("cartId") UUID cartId
  ) {
    cartService.clearCart(cartId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<ErrorDto> handleCartNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("Cart not found"));
  }
  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<ErrorDto> handleCourseNotFound() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Course not found in the cart"));
  }
}
