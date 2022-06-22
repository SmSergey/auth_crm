package com.exceedit.auth.security;

import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest req,
            HttpServletResponse res,
            AccessDeniedException e
    ) throws IOException {
        res.sendError(403);
    }
}
