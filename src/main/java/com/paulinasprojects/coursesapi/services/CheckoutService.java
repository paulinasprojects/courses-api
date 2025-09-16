package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.CheckoutReq;
import com.paulinasprojects.coursesapi.dtos.CheckoutResponse;
import com.paulinasprojects.coursesapi.entities.Order;
import com.paulinasprojects.coursesapi.exceptions.CartEmptyException;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.exceptions.PaymentException;
import com.paulinasprojects.coursesapi.repositories.CartRepository;
import com.paulinasprojects.coursesapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final CartService cartService;
  private final PaymentGateway paymentGateway;

  @Transactional
  public CheckoutResponse checkout(CheckoutReq request)  {
    var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    if (cart.isEmpty()) {
     throw new CartEmptyException();
    }
    var order = Order.fromCart(cart, authService.getCurrentUser());
    orderRepository.save(order);

   try {
  var session =  paymentGateway.createCheckoutSession(order);

     cartService.clearCart(cart.getId());

     return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
   }
   catch(PaymentException ex) {
     orderRepository.delete(order);
     throw  ex;
   }
  }
}
