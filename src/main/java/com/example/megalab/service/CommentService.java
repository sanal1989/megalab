package com.example.megalab.service;

import com.example.megalab.DTO.CommentDTO;
import com.example.megalab.entity.Comment;
import com.example.megalab.entity.News;
import com.example.megalab.entity.User;
import com.example.megalab.repository.CommentRepository;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import com.example.megalab.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          NewsRepository newsRepository,
                          JwtTokenProvider jwtTokenProvider) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> addComment(String token, long news, Comment comment) {
        String login = jwtTokenProvider.getUserName(token);
        User user1 = userRepository.findByLogin(login).get();
        News news1;
        try{
            news1 = newsRepository.findById(news).get();
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("News doesn't exist", HttpStatus.BAD_REQUEST);
        }
        comment.setUser(user1);
        comment.setNews(news1);
        commentRepository.save(comment);
        return new ResponseEntity<>("comment save", HttpStatus.OK);
    }

    public ResponseEntity<?> addCommentForComment(String token,
                                                  long comment_id,
                                                  Comment comment) {
        String login = jwtTokenProvider.getUserName(token);
        User user1 = userRepository.findByLogin(login).get();
        Comment comment1;
        try {
            comment1 = commentRepository.findById(comment_id).get();
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("Comment doesn't exist", HttpStatus.BAD_REQUEST);
        }
        comment.setUser(user1);
        List<Comment> list = comment1.getCommentForComment();
        list.add(comment);
        commentRepository.save(comment);
        return new ResponseEntity<>("comment save", HttpStatus.OK);
    }

    public List<Comment> getCommentNews(long newsId) {
        News news = newsRepository.findById(newsId).get();
        return commentRepository.findByNews(news);
    }

    public List<CommentDTO> listToListDTO(List<Comment> commentList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setUserDTO(comment.getUser());
            commentDTO.setComment(comment.getComment());
            commentDTO.setCommentForComment(listToListDTO(comment.getCommentForComment()));
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
