package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.*;
import com.paulinasprojects.coursesapi.entities.Role;
import com.paulinasprojects.coursesapi.mappers.UserMapper;
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
  private final PasswordEncoder passwordEncoder;

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
}
