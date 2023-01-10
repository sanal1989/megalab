package com.example.megalab.controller;

import com.example.megalab.DTO.TokenDTO;
import com.example.megalab.entity.User;
import com.example.megalab.DTO.AuthenticationRequestDTO;
import com.example.megalab.security.JwtTokenProvider;
import com.example.megalab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sp/auth")
public class AuthenticationRestController {

    Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);
    private final AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        UserService userService,
                                        JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Sing in user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sing in success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "invalide login/password combination",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Parameter(description = "UserDTO send to Sing in") @RequestBody AuthenticationRequestDTO requestDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getLogin(), requestDTO.getPassword()));
            User user = userService.findByLogin(requestDTO.getLogin())
                    .orElseThrow(()-> new UsernameNotFoundException("User doesn't exist"));
            String token = jwtTokenProvider.createToken(requestDTO.getLogin(), user.getRole().name());
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setLogin(requestDTO.getLogin());
            tokenDTO.setToken(token);
            logger.info("User login:" + requestDTO.getLogin());
            return ResponseEntity.ok(tokenDTO);
        }catch(AuthenticationException e){
            return new ResponseEntity<>("invalide login/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "logout success",
                    content = { @Content() })
    })
    @PostMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(httpServletRequest, httpServletResponse, null);
    }
}
