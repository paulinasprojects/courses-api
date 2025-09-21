package com.paulinasprojects.coursesapi.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebhookReq {
  private Map<String, String> headers;
  private String payload;
}
