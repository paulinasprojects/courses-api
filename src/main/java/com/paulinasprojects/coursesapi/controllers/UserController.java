package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.exceptions.*;
import com.paulinasprojects.coursesapi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<?> registerUser(
     @Valid @RequestBody RegisterUserReq req,
     UriComponentsBuilder uriBuilder
  ) {

    var userDto = userService.registerUser(req);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }

  @GetMapping("/{id}")
  public UserDto getUser(@PathVariable Long id
  ) {
    return userService.getUser(id);
  }

  @GetMapping
  public Iterable<UserDto> getAllUsers(
    @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
  ) {
   return userService.getAllUsers(sortBy);
  }

  @PutMapping("/{id}")
  public UserDto updateUser(
     @PathVariable(name = "id") Long id,
     @RequestBody UpdateUserReq request
  ) {
      return userService.updateUser(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable(name = "id") Long id
  ) {
    userService.deleteUser(id);
  }

  @PostMapping("/{id}/change-password")
  public void changePassword(
    @PathVariable(name = "id") Long id,
    @RequestBody ChangePasswordReq req
  ) {
      userService.changePassword(id, req);
  }

  @PostMapping("/{id}/address")
  public AddressDto addAddress(
     @PathVariable(name = "id") Long id,
     @RequestBody AddressDto addressDto
  ) {
    return userService.addAddress(id, addressDto);
  }

  @PutMapping("/{id}/address/{addressId}")
  public AddressDto updateAddress(
          @PathVariable(name = "id") Long id,
          @PathVariable(name = "addressId") Long addressId,
          @Valid @RequestBody UpdateAddressReq req
  ) {
   return userService.updateAddress(id, addressId, req);
  }

  @DeleteMapping("/{id}/address/{addressId}")
  public void  deleteAddress(
          @PathVariable(name = "id") Long id,
          @PathVariable(name = "addressId") Long addressId
  ) {
    userService.deleteAddress(id, addressId);
  }

  @PostMapping("/{id}/profile")
  public ResponseEntity<ProfileDto> addProfile(
          @PathVariable(name = "id") Long id,
          @RequestBody ProfileDto profileDto,
          UriComponentsBuilder uriBuilder
  ) {
   var AddedProfileDto = userService.addProfile(id, profileDto);

    var uri = uriBuilder.path("/users/{id}/profile").buildAndExpand(AddedProfileDto.getId()).toUri();
    return ResponseEntity.created(uri).body(AddedProfileDto);
  }

  @PutMapping("/{id}/profile")
  public ProfileDto updateProfile (
          @PathVariable(name = "id") Long id,
          @RequestBody UpdateProfileReq req
  ) {
    return userService.updateProfile(id, req);
  }

  @DeleteMapping("/{id}/profile")
  public void deleteProfile(
          @PathVariable(name = "id") Long id
  ) {
    userService.deleteProfile(id);
  }

  @ExceptionHandler(DuplicateUserException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateUser() {
    return ResponseEntity.badRequest().body(
            Map.of("email", "Email is already registered")
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Void> handleUserNotFound() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Void> handleAccessDenied() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(AddressNotFoundException.class)
  public ResponseEntity<Void> handleAddressNotFound() {
     return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(ProfileNotFoundException.class)
  public ResponseEntity<Void> handleProfileNotFound() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AddressNotForThisUser.class)
  public ResponseEntity<Map<String, String>> handleAddressNotForUser() {
    return ResponseEntity.badRequest().body(
            Map.of("address", "This address does not belong to you")
    );
  }
}
