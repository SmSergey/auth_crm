package com.exceedit.auth.security.filters;

import com.exceedit.auth.utils.crypto.jwt.JwtTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtCsrfFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository tokenRepository;

    private final HandlerExceptionResolver resolver;

    public JwtCsrfFilter(CsrfTokenRepository tokenRepository, HandlerExceptionResolver resolver) {
        this.tokenRepository = tokenRepository;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
