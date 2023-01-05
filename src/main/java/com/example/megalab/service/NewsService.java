package com.example.megalab.service;

import com.example.megalab.DTO.NewsDTO;
import com.example.megalab.DTO.UserDTO;
import com.example.megalab.entity.News;
import com.example.megalab.entity.Rubric;
import com.example.megalab.entity.User;
import com.example.megalab.repository.CommentRepository;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import com.example.megalab.security.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityManager enitityManager;
    private final CommentRepository commentRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository,
                       UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       EntityManager enitityManager,
                       CommentRepository commentRepository) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.enitityManager = enitityManager;
        this.commentRepository = commentRepository;
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

    public ResponseEntity<?> filterNews(String sport,
                                        String politics,
                                        String stars,
                                        String art,
                                        String fashion) {
        CriteriaBuilder cb = enitityManager.getCriteriaBuilder();
        CriteriaQuery<News> query = cb.createQuery(News.class);
        Root<News> root = query.from(News.class);
        List<Predicate> predicates = new ArrayList<>();

        if(!sport.equals("")) {
            predicates.add(cb.equal(root.get("rubric"), Rubric.getRubric(sport)));
        }
        if(!politics.equals("")) {
            predicates.add(cb.equal(root.get("rubric"), Rubric.getRubric(politics)));
        }
        if(!stars.equals("")) {
            predicates.add(cb.equal(root.get("rubric"), Rubric.getRubric(stars)));
        }
        if(!art.equals("")) {
            predicates.add(cb.equal(root.get("rubric"), Rubric.getRubric(art)));
        }
        if(!fashion.equals("")) {
            predicates.add(cb.equal(root.get("rubric"), Rubric.getRubric(fashion)));
        }

        query.select(root).where(cb.or(predicates.toArray(new Predicate[0])));
        List<News> news = enitityManager.createQuery(query).getResultList();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    public News getNews(long newsId) {
        return newsRepository.findById(newsId).get();
    }

    public ResponseEntity<?> deleteNews(String token, long newsId) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        News news = newsRepository.findById(newsId).get();
        if(user.getId() == news.getUser().getId()){
            commentRepository.deleteByNews(news);
            newsRepository.deleteById(newsId);
            return new ResponseEntity<>("News delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("News doesn't delete", HttpStatus.BAD_REQUEST);
    }

    public List<News> findNews(String header) {
        return newsRepository.findByHeaderContains(header);
    }

    public NewsDTO newsToNewsDTO(News news){
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(news.getId());
        newsDTO.setHeader(news.getHeader());
        newsDTO.setUser(news.getUser());
        newsDTO.setRubric(news.getRubric());
        newsDTO.setDescription(news.getDescription());
        newsDTO.setContent(news.getContent());
        newsDTO.setPicture(news.getPicture());
        return  newsDTO;
    }

    public List<NewsDTO> listToListDTO(List<News> newsList) {
        List<NewsDTO> newsDTOList = new ArrayList<>();
        for (int i = 0; i < newsList.size(); i++) {
            News news = newsList.get(i);
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setId(news.getId());
            newsDTO.setHeader(news.getHeader());
            newsDTO.setUser(news.getUser());
            newsDTO.setRubric(news.getRubric());
            newsDTO.setDescription(news.getDescription());
            newsDTO.setContent(news.getContent());
            newsDTO.setPicture(news.getPicture());
            newsDTOList.add(newsDTO);
        }
        return newsDTOList;
    }
}
