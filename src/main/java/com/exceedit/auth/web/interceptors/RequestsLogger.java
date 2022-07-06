package com.exceedit.auth.web.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RequestsLogger implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RequestsLogger.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " " + response.getStatus());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
