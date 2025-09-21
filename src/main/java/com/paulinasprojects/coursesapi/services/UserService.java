package com.paulinasprojects.coursesapi.services;


import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.entities.Profile;
import com.paulinasprojects.coursesapi.entities.Role;
import com.paulinasprojects.coursesapi.entities.User;
import com.paulinasprojects.coursesapi.exceptions.*;
import com.paulinasprojects.coursesapi.mappers.AddressMapper;
import com.paulinasprojects.coursesapi.mappers.ProfileMapper;
import com.paulinasprojects.coursesapi.mappers.UserMapper;
import com.paulinasprojects.coursesapi.repositories.AddressRepository;
import com.paulinasprojects.coursesapi.repositories.ProfileRepository;
import com.paulinasprojects.coursesapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AddressMapper addressMapper;
  private final AddressRepository addressRepository;
  private final ProfileRepository profileRepository;
  private final ProfileMapper profileMapper;

  public Iterable<UserDto> getAllUsers(String sortBy) {
    if (!Set.of("name", "email").contains(sortBy)) {
      sortBy = "name";
    }

    return userRepository.findAll(Sort.by(sortBy))
            .stream()
            .map(userMapper::toDto)
            .toList();
  }

  public UserDto getUser(Long userId) {
    var user = findUserById(userId);
    return userMapper.toDto(user);
  }


  public UserDto registerUser(RegisterUserReq req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new DuplicateUserException();
    }
    var user = userMapper.toEntity(req);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    userRepository.save(user);

    return userMapper.toDto(user);
  }

  public UserDto updateUser(Long id, UpdateUserReq req) {
    var user = findUserById(id);
    userMapper.updateUser(req, user);
    userRepository.save(user);

    return userMapper.toDto(user);
  }

  public void deleteUser(Long userId) {
    var user = findUserById(userId);
    userRepository.delete(user);
  }

  public void changePassword(Long userId, ChangePasswordReq req) {
    var user = findUserById(userId);
    if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
      throw new AccessDeniedException("Password does not match");
    }
    user.setPassword(req.getNewPassword());
    userRepository.save(user);
  }

  public AddressDto addAddress(Long userId, AddressDto addressDto) {
    var user = findUserById(userId);
    var address = addressMapper.toEntity(addressDto);
    address.setUser(user);
    user.getAddresses().add(address);

    var savedAddress = addressRepository.save(address);
    return addressMapper.toAddressDto(savedAddress);
  }

  public AddressDto updateAddress(Long userId, Long addressId, UpdateAddressReq req) {
    var user = findUserById(userId);
    var address = user.getAddresses().stream().filter((a -> a.getId().equals(addressId)))
            .findFirst()
            .orElseThrow(AddressNotFoundException::new);
    addressMapper.updateSingleAddress(req, address);
    addressRepository.save(address);
    return addressMapper.toAddressDto(address);
  }

  public void deleteAddress(Long userId, Long addressId) {
    var user = findUserById(userId);
    var address = addressRepository.findById(addressId).orElseThrow(AddressNotFoundException::new);
    if (!address.getUser().getId().equals(userId)) {
      throw new AddressNotForThisUser();
    }
    user.getAddresses().remove(address);
    addressRepository.delete(address);
  }

  public ProfileDto addProfile(Long userId, ProfileDto profileDto) {
    var user = findUserById(userId);
    var profile = profileMapper.toEntity(profileDto);
    profile.setUser(user);

    var savedProfile = profileRepository.save(profile);
    return profileMapper.toProfileDto(savedProfile);
  }

  public ProfileDto updateProfile(Long userId, UpdateProfileReq req) {
    var user = findUserById(userId);
    var profile = user.getProfile();
    if (profile == null) {
      profile = Profile.builder()
              .user(user)
              .bio(req.getBio())
              .phoneNumber(req.getPhoneNumber())
              .dateOfBirth(req.getDateOfBirth())
              .loyaltyPoints(req.getLoyaltyPoints())
              .build();
      profileRepository.save(profile);
      user.setProfile(profile);
      userRepository.save(user);
    } else {
      profileMapper.updateProfile(req, profile);
      profileRepository.save(profile);
    }

    return profileMapper.toProfileDto(profile);
  }

  public void deleteProfile(Long userId) {
    var user = findUserById(userId);
    var profile = user.getProfile();
    if (profile == null) {
      throw new ProfileNotFoundException();
    }
    user.setProfile(null);
    userRepository.save(user);
    profileRepository.delete(profile);
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
  }
}
