package com.exceedit.auth.web.controller.api.oauth;


import com.exceedit.auth.data.models.UserTokens;
import com.exceedit.auth.data.repository.UserCodeRepository;
import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.data.repository.UserTokensRepository;
import com.exceedit.auth.utils.crypto.jwt.JwtTokenRepository;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.advices.annotations.ApiException;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.oauth.OauthLoginParams;
import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@ApiException
@RequestMapping("api/auth")
public class TokenController {

    private final Logger logger = LoggerFactory.getLogger(TokenController.class);
    private final String AUTHORIZATION_HEADER_NAME = "Authorization";

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private UserTokensRepository userTokensRepository;


    @PostMapping("/login")
    public ResponseEntity<String> getTokens(@RequestBody OauthLoginParams loginParams) {
        val loginToken = loginParams.getLogin_token();
        val userCode = userCodeRepository.findByCode(loginToken);
        if (userCode == null) {
            return new ApiResponse()
                    .setStatus(400)
                    .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
        }

        val user = userRepository.findById(userCode.getUserId());
        if (user.isEmpty()) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.USER_NOT_FOUND).build();

        val userId = user.get().getId().toString();
        val tokens = jwtTokenRepository.generateTokens(userId);
        val userTokens = new UserTokens(
                user.get().getId(),
                tokens.getAccessToken(),
                tokens.getRefreshToken()
        );

        userCodeRepository.delete(userCode);
        userTokensRepository.save(userTokens);

        return new ApiResponse()
                .setStatus(200)
                .addField("data",
                        new JSONObject()
                                .put("accessInfo", new JSONObject()
                                        .put("access_token", tokens.getAccessToken())
                                        .put("refresh_token", tokens.getRefreshToken())
                                )
                                .put("user", new JSONObject()
                                        .put("_id", user.get().get_id())
                                        .put("permissions", new JSONObject()
                                                .put("GLOBAL", 31)
                                                .put("FULL", 1)
                                                .put("FM", 0)
                                                .put("BA", 0)
                                                .put("T", 0)
                                                .put("STAFF", 0)
                                                .put("R", 0)
                                                .put("AN", 0)
                                        )
                                        .put("team", "5fd8977aec290cace76dc08d")
                                        .put("fullName", user.get().getFullName())
                                        .put("allowedAccounts", "[]")
                                        .put("managedTeams", "null")
                                        .put("isFull", true)
                                )
                ).shouldNotify(false).build();
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkToken(HttpServletRequest request, HttpServletResponse response) {
        val authorization = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authorization == null || authorization.split(" ")[1] == null) {
            return new ApiResponse()
                    .setStatus(400)
                    .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
        }

        val tokenString = authorization.split(" ")[1];
        val isTokenValid = jwtTokenRepository.isTokenValid(tokenString);
        if(!isTokenValid) return new ApiResponse()
                .setStatus(400)
                .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();

        val tokensRecord = userTokensRepository.findByAccessToken(tokenString);
        if(tokensRecord == null) return new ApiResponse()
                .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN)
                .setStatus(400).build();

        val tokenData = jwtTokenRepository.parseToken(tokenString);
        try {
            val userId = new JSONObject(tokenData.get("user").toString()).get("userId");
            val user = userRepository.findById(Long.valueOf(userId.toString()));
            if (user.isEmpty()) return new ApiResponse()
                    .setStatus(400)
                    .setMessage(ErrorMessages.USER_NOT_FOUND).build();

            return new ApiResponse()
                    .setStatus(200)
                    .addField("_id", user.get().get_id())
                    .addField("fullName", user.get().getFullName())
                    .addField("permissions", new JSONObject()
                            .put("FULL", 1)
                            .put("FM", 0)
                            .put("BA", 0)
                            .put("T", 0)
                            .put("STAFF", 0)
                            .put("R", 0)
                            .put("AN", 0)
                    )
                    .addField("isFull", true)
                    .build();

        } catch (JSONException err) {
            logger.error("Couldn't parse token data " + err);
            return new ApiResponse()
                    .setStatus(400)
                    .setMessage("token is not valid").build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        val authorization = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authorization == null || authorization.split(" ")[1] == null) {
            return new ApiResponse()
                    .setStatus(400)
                    .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
        }

        val tokenString = authorization.split(" ")[1];

        val tokenRecord = userTokensRepository.findByAccessToken(tokenString);

        if(tokenRecord != null) {
            userTokensRepository.delete(tokenRecord);
        }

        return new ApiResponse()
                .setStatus(200)
                .shouldNotify(false)
                .setMessage(SuccessMessages.SUCCESS).build();
    }
}
