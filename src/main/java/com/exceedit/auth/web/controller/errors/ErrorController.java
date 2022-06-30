package com.exceedit.auth.web.controller.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping(value = "/error")
    public ModelAndView handleError(
            HttpServletRequest request,
            HttpServletResponse response) {

        switch (response.getStatus()) {
            case 400: {
                return new ModelAndView("errors/400");
            }
            case 403: {
                return new ModelAndView("errors/403");
            }
            case 404: {
                return new ModelAndView("errors/404");
            }
            default: {
                return new ModelAndView("errors/500");
            }
        }
    }
}

