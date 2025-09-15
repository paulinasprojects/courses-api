package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
