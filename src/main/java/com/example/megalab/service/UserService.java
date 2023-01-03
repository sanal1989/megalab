package com.example.megalab.service;

import com.example.megalab.DTO.UserDTO;
import com.example.megalab.entity.News;
import com.example.megalab.entity.Role;
import com.example.megalab.entity.User;
import com.example.megalab.repository.NewsRepository;
import com.example.megalab.repository.UserRepository;
import com.example.megalab.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NewsRepository newsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       NewsRepository newsRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.newsRepository = newsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> saveUser(User user){
        Map<Object, Object> response = new HashMap<>();
        if(userRepository.findByLogin(user.getLogin()).isEmpty()){
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setLogin(user.getLogin());
            userDTO.setImage(null);
            response.put("User", userDTO);
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

    public ResponseEntity<?> uploadImage(String token ,MultipartFile file){
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        try {
            user.setImage(file.getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            return new ResponseEntity("Image don't uploaded: ", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Image uploaded successfully: ", HttpStatus.OK);

    }

    public byte[] getImage(String token) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        byte[] image = user.getImage();
        return image;
    }

    public ResponseEntity<?> deleteImageByUser(String token){
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        user.setImage(null);
        userRepository.save(user);
        return new ResponseEntity<>("image was delete", HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(String token, User user) {
        String login = jwtTokenProvider.getUserName(token);
        User userFromDataBase = userRepository.findByLogin(login).get();
        if(!userRepository.findByLogin(user.getLogin()).isEmpty() &&
            !userFromDataBase.getLogin().equals(user.getLogin())){
            return new ResponseEntity<>("User with this login already exist", HttpStatus.BAD_REQUEST);
        }
        if(user.getFirstName() != null) userFromDataBase.setFirstName(user.getFirstName());
        if(user.getLastName() != null) userFromDataBase.setLastName(user.getLastName());
        if(user.getLogin() != null )userFromDataBase.setLogin(user.getLogin());
        userFromDataBase.setRole(Role.USER);
        userRepository.save(userFromDataBase);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(userFromDataBase.getFirstName());
        userDTO.setLastName(userFromDataBase.getLastName());
        userDTO.setLogin(userFromDataBase.getLogin());
        return ResponseEntity.ok(userDTO);
    }

    public ResponseEntity<?> addLike(String token, long newsId) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        News news = newsRepository.findById(newsId).get();
        Set<News> newsSet = user.getNews();
        newsSet.add(news);
        userRepository.save(user);
        return new ResponseEntity<>("like add", HttpStatus.OK);
    }

    public Set<News> likedUser(String token) {
        String login = jwtTokenProvider.getUserName(token);
        User user = userRepository.findByLogin(login).get();
        return user.getNews();
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
