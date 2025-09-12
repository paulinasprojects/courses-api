package com.paulinasprojects.coursesapi.services;

import com.paulinasprojects.coursesapi.config.JwtConfig;
import com.paulinasprojects.coursesapi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
  private final JwtConfig jwtConfig;

  private Jwt generateToken(User user, long tokenExpiration) {
    var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("email", user.getEmail())
            .add("name", user.getName())
            .add("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
            .build();

    return new Jwt(claims, jwtConfig.getSecretKey());
  }

  public Jwt generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public Jwt generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
            .verifyWith(jwtConfig.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }

  public Jwt parseToken(String token) {
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfig.getSecretKey());
    } catch (JwtException e) {
      return null;
    }
  }
}
