package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.dtos.UpdateUserReq;
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
}
