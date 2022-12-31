package com.example.megalab.service;

import com.example.megalab.entity.News;
import com.example.megalab.entity.Rubric;
import com.example.megalab.entity.User;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import com.example.megalab.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public NewsService(NewsRepository newsRepository,
                       UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public ResponseEntity<?> saveNews(String token,
                                      String header,
                                      String description,
                                      String content,
                                      String rubric,
                                      MultipartFile file) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        News news = new News();
        try {
            news.setHeader(header);
            news.setDescription(description);
            news.setContent(content);
            news.setRubric(Rubric.getRubric(rubric));
            news.setPicture(file.getBytes());
            news.setUser(user);
            newsRepository.save(news);
        } catch (IOException e) {
            return new ResponseEntity<>("news don't download", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("news download success", HttpStatus.OK);
    }

    public List<News> getUserNews(String token) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        return newsRepository.findByUser(user);
    }
}
