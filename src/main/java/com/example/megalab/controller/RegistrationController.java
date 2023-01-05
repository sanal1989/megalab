package com.example.megalab.controller;

import com.example.megalab.DTO.UserDTO;
import com.example.megalab.entity.User;
import com.example.megalab.security.JwtTokenProvider;
import com.example.megalab.service.CommentService;
import com.example.megalab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sp")
public class RegistrationController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public RegistrationController(UserService userService,
                                  JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Registration new user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "registration success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "User already exist",
                    content = @Content)
             })
    @PostMapping("/registration")
    public ResponseEntity<?> getLoginPage(@Parameter(description = "UserDto what you sent to registration")@RequestBody UserDTO userDTO){
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        ResponseEntity<?> entity = userService.saveUser(user);
        return entity;
    }
}

