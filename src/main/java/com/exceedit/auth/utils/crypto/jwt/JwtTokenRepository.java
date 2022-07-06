package com.exceedit.auth.utils.crypto.jwt;

import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.val;
import org.bson.internal.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Repository
public class JwtTokenRepository {

    private Logger logger = LoggerFactory.getLogger(JwtTokenRepository.class);

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.authorization.header.name}")
    private String AUTHORIZATION_HEADER_NAME;

    @Value("#{new Long('${jwt.token.access.expiration.min}')}")
    private Long ACCESS_TOKEN_EXPIRATION_MIN;

    @Value("#{new Long('${jwt.token.refresh.expiration.hour}')}")
    private Long REFRESH_TOKEN_EXPIRATION_HOUR;

    public Tokens generateTokens(String userId) {
        try {
            val payload = new JSONObject();
            payload.put("userId", userId);

            val accessToken = generateToken(SECRET, ACCESS_TOKEN_EXPIRATION_MIN, payload.toString());
            val refreshToken = generateToken(SECRET, REFRESH_TOKEN_EXPIRATION_HOUR, payload.toString());

            return new Tokens(accessToken, refreshToken);

        } catch (JSONException err) {
            logger.error("Can not create tokens, json error - " + err);
            return null;
        }
    }

    public String generateToken(String secret, Long expiration, String payload) {
        val id = UUID.randomUUID()
                .toString()
                .replace("-", "");

        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now()
                .plusMinutes(expiration)
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .claim("user", payload)
                .setId(id)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValid(String tokenString) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(SECRET)
                    .parse(tokenString);

            return true;

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException err) {
            if (err instanceof ExpiredJwtException) {
                logger.error("token is expired");
            }
            if (err instanceof SignatureException) {
                logger.error("token is invalid");
            }
            logger.error(err.getMessage());
            return false;
        }
    }

    public JSONObject parseToken(String tokenString) {
        try {
            val base64EncodedBody = tokenString.split("\\.")[1];
            return new JSONObject(
                    new String(Base64.decode(base64EncodedBody))
            );

        } catch (JSONException | ArrayIndexOutOfBoundsException err) {
            logger.error("Couldn't parse token, err - " + err.getMessage());
        }
        return new JSONObject();
    }

    public String fetchTokenFromRequest(HttpServletRequest request) {
        try {
            val authorization = request.getHeader(AUTHORIZATION_HEADER_NAME);
            if (authorization != null && authorization.split(" ")[1] != null) {
                return authorization.split(" ")[1];
            }
        } catch (Exception err) {
            logger.error("couldn't get token, err - " + err.getMessage());
        }
        return null;
    }

    @Data
    public static class Tokens {
        private final String accessToken;
        private final String refreshToken;

        Tokens(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
