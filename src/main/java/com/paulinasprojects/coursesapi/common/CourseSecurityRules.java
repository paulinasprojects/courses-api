package com.paulinasprojects.coursesapi.common;

import com.paulinasprojects.coursesapi.entities.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CourseSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
     registry.requestMatchers(HttpMethod.GET, "/courses/**").permitAll()
             .requestMatchers(HttpMethod.POST, "/courses/**").hasRole(Role.ADMIN.name())
             .requestMatchers(HttpMethod.PUT, "/courses/**").hasRole(Role.ADMIN.name())
             .requestMatchers(HttpMethod.DELETE, "/courses/**").hasRole(Role.ADMIN.name());
  }
}
