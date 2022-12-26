package com.example.megalab.service;

import com.example.megalab.entity.Comment;
import com.example.megalab.entity.News;
import com.example.megalab.entity.User;
import com.example.megalab.repository.CommentRepository;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          NewsRepository newsRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
    }

    public ResponseEntity<?> addComment(long user, long news, Comment comment) {
        User user1 = userRepository.findById(user).get();
        News news1 = newsRepository.findById(news).get();
        comment.setUser(user1);
        comment.setNews(news1);
        commentRepository.save(comment);
        return new ResponseEntity<>("comment save", HttpStatus.OK);
    }

    public ResponseEntity<?> addCommentForComment(long user_id,
                                                  long comment_id,
                                                  Comment comment) {
        User user1 = userRepository.findById(user_id).get();
        Comment comment1 = commentRepository.findById(comment_id).get();
        comment.setUser(user1);
//        comment1.setCommentForComment(comment1);
        commentRepository.save(comment);
        return new ResponseEntity<>("comment save", HttpStatus.OK);
    }

    public List<Comment> getCommentNews(long newsId) {
        News news = newsRepository.findById(newsId).get();
        return commentRepository.findByNews(news);
    }
}
