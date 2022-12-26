package com.example.megalab.controller;

import com.example.megalab.entity.Comment;
import com.example.megalab.entity.News;
import com.example.megalab.entity.User;
import com.example.megalab.service.CommentService;
import com.example.megalab.service.NewsService;
import com.example.megalab.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class MainController {

    private UserService userService;
    private NewsService newsService;
    private CommentService commentService;

    public MainController(UserService userService,
                          NewsService newsService,
                          CommentService commentService) {
        this.userService = userService;
        this.newsService = newsService;
        this.commentService =commentService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> getLoginPage(@RequestBody User user){
        ResponseEntity<?> entity = userService.saveUser(user);
        return entity;
    }

    @GetMapping("/news")
    public List<News> getSuccessPage(){
        return newsService.findAll();
    }

    @PostMapping("/addNews/{id}")
    public ResponseEntity<?>  addNews(@PathVariable Long id,
                        @RequestParam("image") MultipartFile file,
                        @RequestParam("header") String header,
                        @RequestParam("description") String description,
                        @RequestParam("content") String content,
                        @RequestParam("rubric") String rubric){
        return newsService.saveNews(id,
                                    header,
                                    description,
                                    content,
                                    rubric,
                                    file);
    }

    @GetMapping("/getUserNews/{id}")
    public List<News> getUserNews(@PathVariable long id){
        return newsService.getUserNews(id);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable long id,
                                         @RequestParam("image") MultipartFile file
                                        ) {
        return userService.uploadImage(id,file);
    }

    @GetMapping("/image/{id}")
    @Transactional
    public ResponseEntity<?> getImageByUserId(@PathVariable long id){
        byte[] image = userService.getImage(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<?> deleteImageByUserId(@PathVariable long id){
        return userService.deleteImageByUserId(id);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id,
                                        @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @GetMapping("/like/{userId}/{newsId}")
    public ResponseEntity<?> addLike(@PathVariable long userId,
                                     @PathVariable long newsId){
        return userService.addLike(userId, newsId);
    }

    @GetMapping("/likedUser/{id}")
    @Transactional
    public List<News> likedUser(@PathVariable long id){
        Set<News> news = userService.likedUser(id);
        List<News> list = new ArrayList<>(news);
        for (News i: list) {
            System.out.println(i);
        }
        return list;
    }

    @PostMapping("/addComment/{userId}/{newsId}")
    public ResponseEntity<?> addComment(@PathVariable long userId,
                                        @PathVariable long newsId,
                                        @RequestBody Comment comment){
        System.out.println(comment);
        return commentService.addComment(userId, newsId, comment);
    }

    @PostMapping("/addCommentToComment/{userId}/{commentId}")
    public ResponseEntity<?> addCommentToComment(@PathVariable long userId,
                                        @PathVariable long commentId,
                                        @RequestBody Comment comment){
        return commentService.addCommentForComment(userId, commentId, comment);
    }

    @GetMapping("getComment/{newsId}")
    @Transactional
    public List<Comment> getComment(@PathVariable long newsId){
        return commentService.getCommentNews(newsId);
    }
}
