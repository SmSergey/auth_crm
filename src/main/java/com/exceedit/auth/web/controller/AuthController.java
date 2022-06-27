package com.exceedit.auth.web.controller;

import com.exceedit.auth.model.Code;
import com.exceedit.auth.model.UserCode;
import com.exceedit.auth.repository.ClientRepository;
import com.exceedit.auth.repository.UserCodeRepository;
import com.exceedit.auth.repository.UserRepository;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.web.dto.AuthParamsDto;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(path = "api/oauth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    @GetMapping("/login")
    public ModelAndView authorize(
            @RequestParam(name = "client_id", required = false) String clientId,
            @RequestParam(name = "redirect_url", required = false) String redirectUrl,
            @RequestParam(name = "response_type", required = false) String responseType,
            @RequestParam(name = "scope", required = false) String scope,
            HttpServletResponse response
    ) {
        return new ModelAndView("login")
                .addObject("client_id", clientId)
                .addObject("redirect_url", redirectUrl)
                .addObject("response_type", responseType)
                .addObject("scope", scope);
    }

    @PostMapping("/login")
    public ModelAndView login(
            AuthParamsDto authParams,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        val securityContext = SecurityContextHolder.getContext();
        val user = userRepository.findByEmail(authParams.getEmail());
        val authToken = new UsernamePasswordAuthenticationToken(
                authParams.getEmail(),
                authParams.getPassword());
        try {
            securityContext.setAuthentication(
                    authManager.authenticate(authToken)
            );
        } catch (AuthenticationException err) {
            return new ModelAndView("login")
                    .addObject("code", authParams.getCode())
                    .addObject("emailError", ErrorMessages.INCORRECT_AUTH);
        }

        UserCode userCode = new UserCode();
        userCode.setUserId(user.get_id());
        userCode.setCode(new Code().getCodeString());

        userCodeRepository.save(userCode);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/**")
    public ModelAndView redirectToLogin() {
        return new ModelAndView("redirect:/api/oauth/login");
    }
}
