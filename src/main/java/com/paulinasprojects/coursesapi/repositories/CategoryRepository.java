package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
