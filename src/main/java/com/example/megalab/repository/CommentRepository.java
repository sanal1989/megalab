package com.example.megalab.repository;

import com.example.megalab.entity.Comment;
import com.example.megalab.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNews(News news);
}
