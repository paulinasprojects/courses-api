package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.CartDto;
import com.paulinasprojects.coursesapi.dtos.CartItemDto;
import com.paulinasprojects.coursesapi.entities.Cart;
import com.paulinasprojects.coursesapi.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "items", source = "items")
  @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
  CartDto toDto(Cart cart);

  @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
  CartItemDto cartItemToDto(CartItem cartItem);
}
