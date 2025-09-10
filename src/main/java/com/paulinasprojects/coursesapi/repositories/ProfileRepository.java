package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
