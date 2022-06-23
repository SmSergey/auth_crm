package com.exceedit.auth.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BankController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @GetMapping("/")
    public ModelAndView transfer() {
        return new ModelAndView("public-page");
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/private")
    public ModelAndView transferPrivate() {
        return new ModelAndView("admin-page");
    }

    @GetMapping("/access-denied")
    public ModelAndView accessDeniedPage() {
        return new ModelAndView("403");
    }
}
