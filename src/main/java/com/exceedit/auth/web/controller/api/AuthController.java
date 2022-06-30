package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.data.models.Code;
import com.exceedit.auth.data.models.UserCode;
import com.exceedit.auth.data.repository.ClientRepository;
import com.exceedit.auth.data.repository.UserCodeRepository;
import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.utils.crypto.jwt.JwtTokenRepository;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.advices.annotations.ApiException;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.AuthParams;
import com.exceedit.auth.web.dto.ValidateTokenParams;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@ApiException
@RestController
@RequestMapping("api/oauth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @PostMapping(value = "/login")
    public ModelAndView login(
             AuthParams authParams,
            @RequestParam String redirectUrl,
            @RequestParam String clientID,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authParams.getEmail(),
                            authParams.getPassword()
                    )
            );
        } catch (AuthenticationException err) {
            return new ModelAndView("login")
                    .addObject("emailError", ErrorMessages.BAD_CREDS);
        }

        val user = userRepository.findByEmail(authParams.getEmail());
        val code = new Code();

        userCodeRepository.save(
                new UserCode(user.getId(), code.getCodeString())
        );

        return new ModelAndView("redirect:/api/oauth/redirect?redirectUrl=" + redirectUrl + "&login_token=" + code.getCodeString());
    }

    @GetMapping("/tokens")
    public ResponseEntity<String> getTokens(@RequestParam @NotEmpty String authCode, HttpServletResponse response) {
        val userCode = userCodeRepository.findByCode(authCode);
        logger.info(authCode);
        if (userCode == null) {
            return new ApiResponse()
                    .setStatus(404)
                    .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
        }

        val user = userRepository.findById(userCode.getUserId());
        if (user.isEmpty()) {
            return new ApiResponse()
                    .setStatus(404)
                    .setMessage(ErrorMessages.USER_NOT_FOUND).build();
        } else {
            userCodeRepository.delete(userCode);
            val userId = user.get().getId().toString();
            val tokens = jwtTokenRepository.generateTokens(userId);

            return new ApiResponse()
                    .setMessage(SuccessMessages.SUCCESS)
                    .addField("accessToken", tokens.getAccessToken())
                    .addField("refreshToken", tokens.getRefreshToken())
                    .setStatus(200).build();
        }
    }

    @ResponseBody
    @PostMapping(value = "/tokens")
    public ResponseEntity<String> validateToken(@RequestBody @Valid ValidateTokenParams params) {
        val accessToken = params.getAccessToken();

        if (!jwtTokenRepository.isTokenValid(accessToken)) {
            return new ApiResponse()
                    .setStatus(403)
                    .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
        }
        val tokenData = jwtTokenRepository.parseToken(accessToken);

        return new ApiResponse()
                .setStatus(200)
                .setMessage(tokenData.toString()).build();
    }

    @GetMapping("/redirect")
    public RedirectView oauthBackRedirect(@RequestParam String redirectUrl, @RequestParam String login_token) {
        return new RedirectView(redirectUrl + "?login_token=" + login_token);
    }
}
