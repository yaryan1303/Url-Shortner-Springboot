package com.url.shortner.service;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {
  // Authorization -> Bearer <TOKEN>

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationsMs;

  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }

  public String generateToken(UserDetailsImpl userDetails) {
    String username = userDetails.getUsername();
    String roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
        .collect(Collectors.joining(","));

    return Jwts.builder().subject(username).claim("roles", roles).issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + 172800000)).signWith(key()).compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

}
