package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession createCheckoutSession(Order order);
 Optional<PaymentResult> parseWebhookRequest(WebhookReq req);
}
