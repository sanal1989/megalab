package com.example.megalab;

import com.example.megalab.entity.Role;
import com.example.megalab.entity.User;
import com.example.megalab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MegalabApplication {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public MegalabApplication(UserService userService,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(MegalabApplication.class, args);

    }
    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
//            User user1 = new User();
//            user1.setFirstName("t1");
//            user1.setLastName("t1");
//            user1.setRole(Role.USER);
//            user1.setLogin("One");
//            user1.setPassword(passwordEncoder.encode("One"));
//            userService.saveUser(user1);
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user1.getLogin(), user1.getPassword()));

//            User user2 = new User();
//            user2.setFirstName("t2");
//            user2.setLastName("t2");
//            user2.setRole(Role.USER);
//            user2.setLogin("t2");
//            user2.setPassword(passwordEncoder.encode("t2"));
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user2.getLogin(), user2.getPassword()));
//            userService.saveUser(user2);

        };
    }
}
