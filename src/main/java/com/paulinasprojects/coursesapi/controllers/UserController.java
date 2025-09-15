package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.entities.Address;
import com.paulinasprojects.coursesapi.entities.Profile;
import com.paulinasprojects.coursesapi.entities.Role;
import com.paulinasprojects.coursesapi.mappers.AddressMapper;
import com.paulinasprojects.coursesapi.mappers.ProfileMapper;
import com.paulinasprojects.coursesapi.mappers.UserMapper;
import com.paulinasprojects.coursesapi.repositories.AddressRepository;
import com.paulinasprojects.coursesapi.repositories.ProfileRepository;
import com.paulinasprojects.coursesapi.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AddressMapper addressMapper;
  private final PasswordEncoder passwordEncoder;
  private final AddressRepository addressRepository;
  private final ProfileRepository profileRepository;
  private final ProfileMapper profileMapper;

  @PostMapping
  public ResponseEntity<?> registerUser(
          @Valid @RequestBody RegisterUserReq req,
          UriComponentsBuilder uriBuilder
          ) {
    if (userRepository.existsByEmail(req.getEmail())) {
      return ResponseEntity.badRequest().body(
              new ErrorDto("Email is already registered")
      );
    }
    var user = userMapper.toEntity(req);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    userRepository.save(user);
    var userDto = userMapper.toDto(user);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(
     @PathVariable Long id
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @GetMapping
  public Iterable<UserDto> getAllUsers(
    @RequestParam(required = false, defaultValue = "", name = "sort") String sort
  ) {
    if (!Set.of("name", "email").contains(sort)) {
      sort = "name";
    }

    return userRepository.findAll(Sort.by(sort))
            .stream()
            .map(userMapper::toDto)
            .toList();
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
          @PathVariable(name = "id") Long id,
          @RequestBody UpdateUserReq request
          ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    } else {
      userMapper.updateUser(request, user);
      userRepository.save(user);

      return ResponseEntity.ok(userMapper.toDto(user));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
          @PathVariable(name = "id") Long id
  ) {
    var user = userRepository.findById(id).orElse(null);

    if (user == null) {
      return ResponseEntity.notFound().build();
    } else {
      userRepository.delete(user);
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("/{id}/change-password")
  public ResponseEntity<Void> changePassword(
          @PathVariable Long id,
          @RequestBody ChangePasswordReq req
          ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    if (!user.getPassword().equals(req.getOldPassword())) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    user.setPassword(req.getNewPassword());
    userRepository.save(user);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/address")
  public ResponseEntity<AddressDto> addAddress(
          @PathVariable(name = "id") Long id,
          @RequestBody AddressDto addressDto
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    Address address;
     if (addressDto.getId() != null) {
      address = user.getAddresses().stream().filter(a -> a.getId().equals(addressDto.getId()))
              .findFirst()
              .orElse(null);

      if (address == null) {
        return ResponseEntity.notFound().build();
      }
       addressMapper.updateAddress(addressDto, address);
     } else {
       address = new Address();
       addressMapper.updateAddress(addressDto, address);
       address.setUser(user);
       user.getAddresses().add(address);
     }
     addressRepository.save(address);
     return ResponseEntity.ok(addressMapper.toAddressDto(address));
  }

  @PutMapping("/{id}/address/{addressId}")
  public ResponseEntity<AddressDto> updateAddress(
          @PathVariable(name = "id") Long id,
          @PathVariable(name = "addressId") Long addressId,
         @Valid @RequestBody UpdateAddressReq req
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    var address = user.getAddresses().stream().filter(a -> a.getId().equals(addressId))
            .findFirst()
            .orElse(null);
    if (address == null) {
      return ResponseEntity.notFound().build();
    }

    addressMapper.updateSingleAddress(req, address);
    addressRepository.save(address);
    return ResponseEntity.ok(addressMapper.toAddressDto(address));
  }

  @DeleteMapping("/{id}/address/{addressId}")
  public ResponseEntity<Void> deleteAddress(
          @PathVariable(name = "id") Long id,
          @PathVariable(name = "addressId") Long addressId
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    var address = addressRepository.findById(addressId).orElse(null);
    if (address == null) {
      return ResponseEntity.notFound().build();
    } else {
      addressRepository.delete(address);
      return ResponseEntity.noContent().build();
    }
  }

  @PostMapping("/{id}/profile")
  public ResponseEntity<ProfileDto> addProfile(
          @PathVariable(name = "id") Long id,
          @RequestBody ProfileDto profileDto,
          UriComponentsBuilder uriBuilder
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
   var profile = profileMapper.toEntity(profileDto);
    profile.setUser(user);
    profileRepository.save(profile);
    profileDto.setId(profile.getId());

    var uri = uriBuilder.path("/users/{id}/profile").buildAndExpand(profileDto.getId()).toUri();
    return ResponseEntity.created(uri).body(profileDto);
  }

  @PutMapping("/{id}/profile")
  public ResponseEntity<ProfileDto> updateProfile (
          @PathVariable(name = "id") Long id,
          @RequestBody UpdateProfileReq req
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
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
    return ResponseEntity.ok(profileMapper.toProfileDto(profile));
  }

  @DeleteMapping("/{id}/profile")
  public ResponseEntity<Void> deleteProfile(
          @PathVariable(name = "id") Long id
  ) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    var profile = user.getProfile();
    if (profile == null) {
      return ResponseEntity.notFound().build();
    }

    user.setProfile(null);
    userRepository.save(user);
    profileRepository.delete(profile);

    return ResponseEntity.noContent().build();
  }
}
