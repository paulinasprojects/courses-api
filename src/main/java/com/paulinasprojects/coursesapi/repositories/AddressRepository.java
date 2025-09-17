package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Address;
import com.paulinasprojects.coursesapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
