package com.exceedit.auth.security.services;

import com.exceedit.auth.data.repository.UserRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class userService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(userService.class);
    private final String ADMIN_ROLE = "ADMIN";
    private final String USER_ROLE = "USER";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        val user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s wasn't fund", email));
        }
        return new User(user.getEmail(), user.getPassword(), getAuthority(ADMIN_ROLE));
    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}