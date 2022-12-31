package com.example.megalab.controller;

import com.example.megalab.entity.User;
import com.example.megalab.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/imageUser")
    public ResponseEntity<?> uploadImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @RequestParam("image") MultipartFile file
    ) {
        return userService.uploadImage(token,file);
    }

    @GetMapping("/imageUser")
    @Transactional
    public ResponseEntity<?> getImageByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        byte[] image = userService.getImage(token);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @DeleteMapping("/imageUser")
    public ResponseEntity<?> deleteImageByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.deleteImageByUser(token);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                   @RequestBody User user){
        return userService.updateUser(token, user);
    }

    @PutMapping("/test")
    public ResponseEntity<?> testUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @RequestBody User user){

        return userService.testUser(token, user);
    }

}
