package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.AddressDto;
import com.paulinasprojects.coursesapi.dtos.UpdateAddressReq;
import com.paulinasprojects.coursesapi.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
   Address toEntity(AddressDto addressDto);

  AddressDto toAddressDto(Address address);

  @Mapping(target = "id", ignore = true)
  void updateAddress(AddressDto addressDto, @MappingTarget Address address);

  void updateSingleAddress(UpdateAddressReq request, @MappingTarget Address address);
}
