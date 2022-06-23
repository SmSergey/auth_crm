package com.exceedit.auth.web.controller;

import com.exceedit.auth.repository.ClientRepository;
import com.exceedit.auth.repository.UserRepository;
import com.exceedit.auth.web.dto.AuthParamsDto;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "oauth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    @GetMapping("/authorize")
    public ModelAndView authorize(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_url") String redirectUrl,
            @RequestParam("response_type") String responseType,
            @RequestParam("scope") String scope
    ) {
        return new ModelAndView("login");
    }

    @GetMapping(path = "/logout")
    public ModelAndView logOut() {
        return new ModelAndView("login");
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ModelAndView login(AuthParamsDto params, final HttpServletRequest request) {

        val user = userRepository.findByEmail(params.getEmail());
        val client = clientRepository.findById(Long.valueOf(params.getClientId()));

        if (user == null) {
            val loginTemplate = new ModelAndView("login");
            return loginTemplate
                    .addObject("code", params.getCode())
                    .addObject("emailError", "User with this email was not found");
        }

        val auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(params.getEmail(), params.getPassword()));

        val sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        val session = request.getSession(false);

        if (session != null) {
            session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        }

        return new ModelAndView("authorize_client")
                .addObject("applicationName", client.get());
    }
}
