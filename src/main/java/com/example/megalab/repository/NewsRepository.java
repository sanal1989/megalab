package com.example.megalab.repository;

import com.example.megalab.entity.News;
import com.example.megalab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByUser(User user);

    List<News> findByHeaderContains(String string);

}
