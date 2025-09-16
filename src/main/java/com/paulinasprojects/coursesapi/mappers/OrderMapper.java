package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.OrderDto;
import com.paulinasprojects.coursesapi.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderDto toOrderDto(Order order);
}
