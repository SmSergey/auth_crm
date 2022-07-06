package com.exceedit.auth.security.filters;

import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.utils.crypto.jwt.JwtTokenRepository;
import com.exceedit.auth.utils.messages.ErrorMessages;
import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenRepository jwtTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public JwtFilter(
            JwtTokenRepository jwtTokenRepository,
            UserRepository userRepository,
            AuthenticationManager authenticationManager
    ) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            filterChain.doFilter(request, response);
            return;
        }

        val tokenString = jwtTokenRepository.fetchTokenFromRequest(request);
        if (tokenString != null && jwtTokenRepository.isTokenValid(tokenString)) {
            try {
                val tokenData = jwtTokenRepository.parseToken(tokenString);
                val userId = new JSONObject(tokenData.get("user").toString()).get("userId").toString();
                val user = userRepository.findById(Long.valueOf(userId));

                if (user.isPresent()) {
                    val authentication = new UsernamePasswordAuthenticationToken(user.get().getEmail(), null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (JSONException err) {
                logger.error(err.getMessage());
            }

            logger.error("bad token");
            response.sendError(403, ErrorMessages.BAD_AUTHORIZATION_TOKEN);
        }
    }
}
