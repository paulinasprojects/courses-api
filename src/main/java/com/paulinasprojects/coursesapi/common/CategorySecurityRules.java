package com.paulinasprojects.coursesapi.common;

import com.paulinasprojects.coursesapi.entities.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CategorySecurityRules implements  SecurityRules{
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
    registry
            .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/categories/**").hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.PUT,"/categories/**").hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE,"/categories/**").hasRole(Role.ADMIN.name());
  }
}
