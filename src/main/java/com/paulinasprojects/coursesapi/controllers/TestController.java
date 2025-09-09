package com.paulinasprojects.coursesapi.controllers;

import com.paulinasprojects.coursesapi.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @RequestMapping("/hello")
  public Message sayHello() {
    return new Message("Hello test");
  }
}
