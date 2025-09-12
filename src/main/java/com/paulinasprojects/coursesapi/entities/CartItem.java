package com.paulinasprojects.coursesapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "quantity")
  private Integer quantity;

  public BigDecimal getTotalPrice() {
    return course.getPrice().multiply(BigDecimal.valueOf(quantity));
  }
}
