package com.exceedit.auth.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BankController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String transfer() {
        return "HELLO PUBLIC PAGE";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/private", method = RequestMethod.GET)
    @ResponseBody
    public String transferPrivate() {
        return "HELLO PRIVATE ONE";
    }
}
