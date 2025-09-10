package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.UserDto;
import com.paulinasprojects.coursesapi.mappers.UserMapper;
import com.paulinasprojects.coursesapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

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
}
