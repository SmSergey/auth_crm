package com.exceedit.auth.web.controller.errors;

import lombok.val;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HttpErrorHandler implements ErrorController {

    @RequestMapping(value = "/error")
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {

        val errorMessage = request
                .getAttribute(RequestDispatcher.ERROR_MESSAGE)
                .toString();

        switch (response.getStatus()) {
            case 400: {
                return new ModelAndView("errors/400")
                        .addObject("message", errorMessage);
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

