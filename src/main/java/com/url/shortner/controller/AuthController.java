package com.url.shortner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.model.User;
import com.url.shortner.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private UserService userService;

  @PostMapping("/public/login")
  public ResponseEntity<?> LoginUser(@RequestBody LoginRequest loginRequest) {

    return ResponseEntity.ok(userService.loginUser(loginRequest));

  }

  @RequestMapping("/public/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(registerRequest.getPassword());
    user.setRole("ROLE_USER");

    userService.registerUser(user);

    return ResponseEntity.ok(user);

  }

}
