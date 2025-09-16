package com.paulinasprojects.coursesapi.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "date_created", insertable = false, updatable = false)
  private LocalDate dateCreated;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<CartItem> items = new LinkedHashSet<>();

  public void clearCart() {
    items.clear();
  }

  public CartItem getCartItem(Long courseId) {
    return items.stream()
            .filter(item -> item.getCourse().getId().equals(courseId))
            .findFirst()
            .orElse(null);
  }

  public CartItem addItemToCart(Course course) {
    var cartItem = getCartItem(course.getId());
    if (cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
    } else {
      cartItem = new CartItem();
      cartItem.setCourse(course);
      cartItem.setQuantity(1);
      cartItem.setCart(this);
      items.add(cartItem);
    }
    return cartItem;
  }

  public void removeCartItem(Long courseId) {
    var cartItem = getCartItem(courseId);
    if (cartItem != null) {
      items.remove(cartItem);
      cartItem.setCart(null);
    }
  }

  public BigDecimal getTotalPrice() {
    return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }
}
