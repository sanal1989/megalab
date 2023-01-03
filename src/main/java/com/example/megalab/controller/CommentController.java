package com.example.megalab.controller;

import com.example.megalab.entity.Comment;
import com.example.megalab.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/addComment/{newsId}")
    public ResponseEntity<?> addComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @PathVariable long newsId,
                                        @RequestBody Comment comment){
        return commentService.addComment(token, newsId, comment);
    }

    @PostMapping("/addCommentToComment/{commentId}")
    public ResponseEntity<?> addCommentToComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable long commentId,
                                                 @RequestBody Comment comment){
        return commentService.addCommentForComment(token, commentId, comment);
    }

    @GetMapping("getComment/{newsId}")
    @Transactional
    public List<Comment> getComment(@PathVariable long newsId){
        return commentService.getCommentNews(newsId);
    }

}
