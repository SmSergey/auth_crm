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
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView transfer() {
        return new ModelAndView("public-page");
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/private", method = RequestMethod.GET)
    public ModelAndView transferPrivate() {
        return new ModelAndView("admin-page");
    }

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public ModelAndView notFoundPage() {
        return new ModelAndView("error-404");
    }

    @GetMapping(path = "/access-denied")
    public ModelAndView accessDeniedPage() {
        return new ModelAndView("access-denied-page");
    }
}
