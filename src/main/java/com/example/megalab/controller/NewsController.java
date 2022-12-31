package com.example.megalab.controller;

import com.example.megalab.entity.News;
import com.example.megalab.security.JwtTokenProvider;
import com.example.megalab.service.NewsService;
import com.example.megalab.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class NewsController {

    private NewsService newsService;
    private UserService userService;

    public NewsController(NewsService newsService,
                          UserService userService) {
        this.newsService = newsService;
        this.userService = userService;
    }

    @GetMapping("/news")
    public List<News> getSuccessPage(){
        return newsService.findAll();
    }

    @PostMapping("/addNews")
    public ResponseEntity<?> addNews(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @RequestParam("image") MultipartFile file,
                                     @RequestParam("header") String header,
                                     @RequestParam("description") String description,
                                     @RequestParam("content") String content,
                                     @RequestParam("rubric") String rubric){
        return newsService.saveNews(token,
                header,
                description,
                content,
                rubric,
                file);
    }

    @GetMapping("/getUserNews")
    public List<News> getUserNews(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return newsService.getUserNews(token);
    }

    @GetMapping("/like/{newsId}")
    public ResponseEntity<?> addLike(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @PathVariable long newsId){
        return userService.addLike(token, newsId);
    }

    @GetMapping("/likedUser")
    @Transactional
    public List<News> likedUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        Set<News> news = userService.likedUser(token);
        List<News> list = new ArrayList<>(news);
        for (News i: list) {
            System.out.println(i);
        }
        return list;
    }
}
