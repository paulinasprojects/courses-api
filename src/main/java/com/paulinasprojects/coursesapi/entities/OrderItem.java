package com.paulinasprojects.coursesapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "unit_price")
  private BigDecimal unitPrice;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "total_price")
  private BigDecimal totalPrice;

  public OrderItem(Order order, Course course, Integer quantity) {
    this.order = order;
    this.course = course;
    this.quantity = quantity;
    this.unitPrice = course.getPrice();
    this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}