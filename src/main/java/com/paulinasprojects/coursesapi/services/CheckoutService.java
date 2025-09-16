package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.dtos.CheckoutReq;
import com.paulinasprojects.coursesapi.dtos.CheckoutResponse;
import com.paulinasprojects.coursesapi.entities.Order;
import com.paulinasprojects.coursesapi.exceptions.CartEmptyException;
import com.paulinasprojects.coursesapi.exceptions.CartNotFoundException;
import com.paulinasprojects.coursesapi.repositories.CartRepository;
import com.paulinasprojects.coursesapi.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final CartService cartService;

  @Value("${websiteUrl}")
  private String websiteUrl;

  @Transactional
  public CheckoutResponse checkout(CheckoutReq request) throws StripeException {
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
     // Create a checkout session
     var builder = SessionCreateParams.builder()
             .setMode(SessionCreateParams.Mode.PAYMENT)
             .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
             .setCancelUrl(websiteUrl + "/checkout-cancel");

     order.getItems().forEach(item -> {
       var lineItem =  SessionCreateParams.LineItem.builder()
               .setQuantity(Long.valueOf(item.getQuantity()))
               .setPriceData(
                       SessionCreateParams.LineItem.PriceData.builder()
                               .setCurrency("usd")
                               .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100 )))
                               .setProductData(
                                       SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                               .setName(item.getCourse().getName())
                                               .build()
                               )
                               .build()
               ).build();
       builder.addLineItem(lineItem);
     });

     var session = Session.create(builder.build());


     cartService.clearCart(cart.getId());

     return new CheckoutResponse(order.getId(), session.getUrl());
   }
   catch(StripeException ex) {
     orderRepository.delete(order);
     throw  ex;
   }
  }
}
