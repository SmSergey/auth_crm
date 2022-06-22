package com.exceedit.auth.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

public class UserInterceptor implements HandlerInterceptor {


    private static Logger log = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (isUserLogged()) {
            addToModelUserDetails(request.getSession());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model) throws Exception {
        if (model != null && !isRedirectView(model)) {
            if (isUserLogged()) {
                addToModelUserDetails(model);
            }
        }
    }

    private void addToModelUserDetails(HttpSession session) {
        log.info("================= addToModelUserDetails1 ============================");
        String loggedUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        session.setAttribute("username", loggedUsername);
        log.info("user(" + loggedUsername + ") session : " + session);
        log.info("================= addToModelUserDetails2 ============================");

    }

    private void addToModelUserDetails(ModelAndView model) {
        log.info("================= addToModelUserDetails11 ============================");
//        String loggedUsername = SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getName();
//        model.addObject("loggedUsername", loggedUsername);
//        log.trace("session : " + model.getModel());
        log.info("================= addToModelUserDetails 22============================");
    }

    public static boolean isRedirectView(ModelAndView mv) {

        String viewName = mv.getViewName();
        if (viewName.startsWith("redirect:/")) {
            return true;
        }

        View view = mv.getView();
        return (view != null && view instanceof SmartView && ((SmartView) view).isRedirectView());
    }

    public static boolean isUserLogged() {
        try {
            return !SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName()
                    .equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }
}
