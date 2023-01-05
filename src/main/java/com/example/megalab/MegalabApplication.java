package com.example.megalab;

import com.example.megalab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

@SpringBootApplication
public class MegalabApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegalabApplication.class, args);

    }
}
