package com.exceedit.auth.web.controller;

import com.exceedit.auth.data.models.Code;
import com.exceedit.auth.data.models.User;
import com.exceedit.auth.data.models.UserCode;
import com.exceedit.auth.data.repository.ClientRepository;
import com.exceedit.auth.data.repository.UserCodeRepository;
import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.web.dto.AuthParams;
import lombok.val;
import org.dom4j.rule.Mode;
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
@RequestMapping()
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

    @PostMapping("/api/oauth/login")
    public ModelAndView login(
            AuthParams authParams,
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

        return new ModelAndView("code")
                .addObject("code", code.getCodeString());
    }

    @GetMapping("api/oauth/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/login");
    }

}
