package com.example.megalab.configuration;


import com.example.megalab.repository.UserRepository;
import com.example.megalab.security.JwtConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

//    private final UserDetailsService userDetailsServiceImp;
    private final JwtConfigure jwtConfigure;
    @Autowired
    public WebSecurityConfig(JwtConfigure jwtConfigure){
        this.jwtConfigure = jwtConfigure;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .requestMatchers("/sp/registration","/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers("/sp/auth/login").permitAll()
                                .anyRequest()
                                .authenticated()
                                .and()
                                .apply(jwtConfigure);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
