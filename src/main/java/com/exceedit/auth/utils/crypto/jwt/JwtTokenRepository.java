package com.exceedit.auth.utils.crypto.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Repository
public class JwtTokenRepository {

    private Logger logger = LoggerFactory.getLogger(JwtTokenRepository.class);

    @Value("jwt.secret")
    private String SECRET;

    @Value("#{new Long('${jwt.token.access.expiration.min}')}")
    private Long ACCESS_TOKEN_EXPIRATION_MIN;

    @Value("#{new Long('${jwt.token.refresh.expiration.hour}')}")
    private Long REFRESH_TOKEN_EXPIRATION_HOUR;


    public String generateTokens(String userId) {
        try {
            val payload = new JSONObject();
            payload.put("userId", userId);

            val accessToken = generateToken(SECRET, ACCESS_TOKEN_EXPIRATION_MIN, payload.toString());
            val refreshToken = generateToken(SECRET, REFRESH_TOKEN_EXPIRATION_HOUR, payload.toString());

            return new JSONObject()
                    .put("accessToken", accessToken)
                    .put("refreshToken", refreshToken)
                    .toString();

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

//    public void saveToken(CsrfToken csrfToken, HttpServletRequest httpServletRequest, HttpServletResponse response) {
//
//    }

//    public CsrfToken loadToken(HttpServletRequest request) {
//
//    }
}
