package com.example.megalab.controller;

import com.example.megalab.DTO.UserDTO;
import com.example.megalab.entity.User;
import com.example.megalab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/sp")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add avatar to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Image don't uploaded",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @PostMapping("/imageUser")
    public ResponseEntity<?> uploadImage(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @Parameter(description = "Image you download to user") @RequestParam("image") MultipartFile file
    ) {
        return userService.uploadImage(token,file);
    }

    @Operation(summary = "Get user's avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = { @Content }),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @GetMapping("/imageUser")
    @Transactional
    public ResponseEntity<?> getImageByUserId(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        byte[] image = userService.getImage(token);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @Operation(summary = "Delete user's avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = { @Content }),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @DeleteMapping("/imageUser")
    public ResponseEntity<?> deleteImageByUserId(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return userService.deleteImageByUser(token);
    }

    @Operation(summary = "Edit user's characters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = { @Content }),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @Parameter(description = "New UserDto characters") @RequestBody UserDTO userDTO){
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setLogin(userDTO.getLogin());
        return userService.updateUser(token, user);
    }

}
