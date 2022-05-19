package com.exceedit.auth.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// to test csrf
@Controller
public class BankController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String transfer() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            System.out.println("LOOOG" + 1111);
            username = principal.toString();
        }
        logger.warn("TAG", username);
        System.out.println("LOOOG   --- " + principal);
        logger.info("Transfer to {}", 45);
        return "HELLO";
    }

    // write - just for test
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestParam("accountNo") final int accountNo, @RequestParam("amount") final int amount) {
        logger.info("Transfer to {}", accountNo);

    }
}
