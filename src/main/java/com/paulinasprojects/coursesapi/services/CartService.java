package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.CartDto;
import com.paulinasprojects.coursesapi.dtos.CartItemDto;
import com.paulinasprojects.coursesapi.entities.Cart;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.CourseNotFoundException;
import com.paulinasprojects.coursesapi.mappers.CartMapper;
import com.paulinasprojects.coursesapi.repositories.CartRepository;
import com.paulinasprojects.coursesapi.repositories.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
  private CartRepository cartRepository;
  private CartMapper cartMapper;
  private CourseRepository courseRepository;

  public CartDto createCart() {
    var cart = new Cart();
    cartRepository.save(cart);
    return cartMapper.toDto(cart);
  }

  public CartItemDto addToCart(UUID cartId, Long courseId) {
    var cart = cartRepository.findById(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }

    var course = courseRepository.findById(courseId).orElse(null);
    if (course == null) {
      throw new CourseNotFoundException();
    }

    var cartItem = cart.addItemToCart(course);

    cartRepository.save(cart);
    return cartMapper.cartItemToDto(cartItem);
  }

  public CartDto getCart(UUID cartId) {
    var cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    return cartMapper.toDto(cart);
  }

  public CartItemDto updateItem(UUID cartId, Long courseId, Integer quantity) {
    var cart = cartRepository.getCartWithItems(cartId).orElse(null);

    if (cart == null) {
      throw new CartNotFoundException();
    }

    var cartItem = cart.getCartItem(courseId);
    if (cartItem == null) {
      throw new CourseNotFoundException();
    }

    cartItem.setQuantity(quantity);
    cartRepository.save(cart);
    return cartMapper.cartItemToDto(cartItem);
  }

  public void removeItem(UUID cartId, Long courseId) {
    var cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }

    cart.removeCartItem(courseId);
    cartRepository.save(cart);
  }

  public void clearCart(UUID cartId) {
    var cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    cart.clearCart();
    cartRepository.save(cart);
  }
}
