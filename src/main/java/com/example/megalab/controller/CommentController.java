package com.example.megalab.controller;

import com.example.megalab.DTO.CommentDTO;
import com.example.megalab.entity.Comment;
import com.example.megalab.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/sp")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Add comment to news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content }),
            @ApiResponse(responseCode = "400", description = "News doesn't exist",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @PostMapping("/addComment/{newsId}")
    public ResponseEntity<?> addComment(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @Parameter(description = "News Id, what comment will be add") @PathVariable long newsId,
                                        @Parameter(description = "String comment")@RequestBody Comment comment){
        return commentService.addComment(token, newsId, comment);
    }

    @Operation(summary = "Add comment to comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content }),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @PostMapping("/addCommentToComment/{commentId}")
    public ResponseEntity<?> addCommentToComment(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @Parameter(description = "Comment id, what you want to addition comment") @PathVariable long commentId,
                                                 @Parameter(description = "String comment")@RequestBody Comment comment){
        return commentService.addCommentForComment(token, commentId, comment);
    }


    @Operation(summary = "Get comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get comment",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }
                    ),
            @ApiResponse(responseCode = "400", description = "News doesn't exist",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Client token has error",
                    content = @Content)
    })
    @GetMapping("/getComment/{newsId}")
    @Transactional
    public ResponseEntity<?> getComment(@Parameter(description = "News id, What you want to get comment")@PathVariable long newsId){
        List<Comment> commentList;
        try{
            commentList = commentService.getCommentNews(newsId);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("News doesn't exist", HttpStatus.BAD_REQUEST);
        }

        List<CommentDTO> commentDTOList = commentService.listToListDTO(commentList);

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

}
