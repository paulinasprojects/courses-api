package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
