package com.example.megalab.controller;

import com.example.megalab.entity.User;
import com.example.megalab.security.JwtTokenProvider;
import com.example.megalab.service.CommentService;
import com.example.megalab.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public RegistrationController(UserService userService,
                                  JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> getLoginPage(@RequestBody User user){
        ResponseEntity<?> entity = userService.saveUser(user);
        return entity;
    }
}

