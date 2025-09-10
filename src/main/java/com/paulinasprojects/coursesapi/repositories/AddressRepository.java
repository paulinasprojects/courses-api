package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
