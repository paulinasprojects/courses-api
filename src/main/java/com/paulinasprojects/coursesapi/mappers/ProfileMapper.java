package com.paulinasprojects.coursesapi.mappers;

import com.paulinasprojects.coursesapi.dtos.ProfileDto;
import com.paulinasprojects.coursesapi.dtos.UpdateProfileReq;
import com.paulinasprojects.coursesapi.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {
  void updateProfile(UpdateProfileReq req, @MappingTarget Profile profile);
  ProfileDto toProfileDto(Profile profile);
  Profile toEntity(ProfileDto profileDto);
}
