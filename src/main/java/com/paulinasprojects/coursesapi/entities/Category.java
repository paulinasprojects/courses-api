package com.paulinasprojects.coursesapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Byte id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "category")
  private Set<Course> courses = new HashSet<>();

  public Category(String name) {
    this.name = name;
  }

  public Category(byte id) {
    this.id = id;
  }
}
