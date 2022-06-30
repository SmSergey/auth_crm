package com.exceedit.auth.web.controller.pages;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PagesController {

    @GetMapping("/login")
    public ModelAndView authorize(
            @RequestParam(name = "clientID", required = false) String clientId,
            @RequestParam(name = "redirectUrl", required = false) String redirectUrl,
            @RequestParam(name = "response_type", required = false) String responseType,
            @RequestParam(name = "scope", required = false) String scope,
            HttpServletResponse response
    ) {
        return new ModelAndView("login")
                .addObject("clientID", clientId)
                .addObject("redirectUrl", redirectUrl)
                .addObject("response_type", responseType)
                .addObject("scope", scope);
    }


    @GetMapping("api/oauth/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/login");
    }

    @ResponseBody
    @GetMapping("/")
    public ModelAndView transfer() {
        return new ModelAndView("public-page");
    }

    @ResponseBody
    @GetMapping("/private")
    public ModelAndView transferPrivate() {
        return new ModelAndView("admin-page");
    }


}
