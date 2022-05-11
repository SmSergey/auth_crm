package com.exceedit.auth.spring;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private Map<String, User> roles = new HashMap<>();

    @PostConstruct
    public void init() {
        roles.put("admin", new User("admin", "{noop}admin1", getAuthority("ROLE_ADMIN")));
        roles.put("user", new User("user", "{noop}user1", getAuthority("ROLE_USER")));
        roles.put("name", new User("name", "{bcrypt}$2a$12$8t8hl2LZLCyq/OYI8b15KO3JawfoTK2wOmjQtLP3MYDuX0yVp3.bK", getAuthority("ROLE_USER")));
    }

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {
        System.out.println("LOOOG " + username);
        return roles.get(username);
    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}