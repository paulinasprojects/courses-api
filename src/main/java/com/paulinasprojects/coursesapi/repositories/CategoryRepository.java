package com.paulinasprojects.coursesapi.repositories;

import com.paulinasprojects.coursesapi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Byte> {
}
