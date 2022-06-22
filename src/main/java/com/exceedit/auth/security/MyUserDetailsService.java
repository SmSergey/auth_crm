package com.exceedit.auth.security;

import com.exceedit.auth.model.User.UserPrincipal;
import com.exceedit.auth.repository.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final String HASH_ALGORITHM_SHA256 = "{bcrypt}";
    private final String ADMIN_ROLE = "ADMIN";
    private final String USER_ROLE = "USER";
    private final Map<String, User> roles = new HashMap<>();

    @PostConstruct
    public void init() {
        val users = userRepository.findAll();
        users.forEach(user -> {
            roles.put("admin", new User(
                    user.getEmail(),
                    //TODO"{ENCODE_ALGORITHM}" + user.getPassword(),
                    "{noop}" + user.getPassword(),
                    getAuthority(ADMIN_ROLE)
            ));
        });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = roles.get("admin");
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}