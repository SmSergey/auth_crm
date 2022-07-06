package com.exceedit.auth.security.services;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class PrincipalService {

    private final Logger logger = LoggerFactory.getLogger(PrincipalService.class);

    public static String getCurrentUserLogin() {
        val principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else return principal.toString();
    }
}
