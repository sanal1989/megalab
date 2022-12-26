package com.example.megalab.service;

import com.example.megalab.entity.News;
import com.example.megalab.entity.Role;
import com.example.megalab.entity.User;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NewsRepository newsRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       NewsRepository newsRepository
                       ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.newsRepository = newsRepository;
    }

    public ResponseEntity<?> saveUser(User user){
        Map<Object, Object> response = new HashMap<>();
        if(userRepository.findByLogin(user.getLogin()).isEmpty()){
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            response.put("User", user);
        }else{
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }

    public ResponseEntity<?> uploadImage(long id,MultipartFile file){
        User user = userRepository.findById(id).get();
        try {
            user.setImage(file.getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            return new ResponseEntity("Image don't uploaded: ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Image uploaded successfully: ", HttpStatus.OK);

    }

    public byte[] getImage(long id) {
        User user = userRepository.findById(id).get();
        byte[] image = user.getImage();
        return image;
    }

    public ResponseEntity<?> deleteImageByUserId(long id){
        User user = userRepository.findById(id).get();
        user.setImage(null);
        userRepository.save(user);
        return new ResponseEntity<>("image was delete", HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(long id, User user) {
        User userFromDataBase = userRepository.findById(id).get();
        if(!userRepository.findByLogin(user.getLogin()).isEmpty() &&
            !userFromDataBase.getLogin().equals(user.getLogin())){
            return new ResponseEntity<>("User with this login already exist", HttpStatus.BAD_REQUEST);
        }
        if(user.getFirstName() != null) userFromDataBase.setFirstName(user.getFirstName());
        if(user.getLastName() != null) userFromDataBase.setLastName(user.getLastName());
        if(user.getLogin() != null )userFromDataBase.setLogin(user.getLogin());
        userRepository.save(userFromDataBase);
        return new ResponseEntity<>(userFromDataBase,HttpStatus.OK);
    }

    public ResponseEntity<?> addLike(long userId, long newsId) {
        User user = userRepository.findById(userId).get();
        News news = newsRepository.findById(newsId).get();
        Set<News> newsSet = user.getNews();
        newsSet.add(news);
        userRepository.save(user);
        return new ResponseEntity<>("like add", HttpStatus.OK);
    }

    public Set<News> likedUser(long id) {
        User user = userRepository.findById(id).get();
        return user.getNews();
    }
}
