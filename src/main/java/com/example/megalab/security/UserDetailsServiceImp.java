package com.example.megalab.security;

import com.example.megalab.entity.User;
import com.example.megalab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImp")
public class UserDetailsServiceImp implements UserDetailsService {

    public final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException("User doesn't exists"));
        return SecurityUser.fromUser(user);
    }
}
