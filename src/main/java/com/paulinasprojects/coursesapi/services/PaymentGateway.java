package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.entities.Order;

public interface PaymentGateway {
  CheckoutSession createCheckoutSession(Order order);
}
