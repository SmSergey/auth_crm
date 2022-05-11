package com.exceedit.auth.web.controller;

import com.exceedit.auth.model.User;
import com.exceedit.auth.repository.UserRepository;
import com.exceedit.auth.web.dto.AuthParamsDto;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(path = "oauth")
public class AuthController {

        @Autowired
        private UserRepository userRepository;

//        @Autowired
//        private PasswordEncoder passwordEncoder;

        @Resource(name="authenticationManager")
        private AuthenticationManager authManager;

//        private  static  final PasswordEncoder b = new BCryptPasswordEncoder();

        @RequestMapping("/authorize")
        public ModelAndView authorize(Model model) {
                ModelAndView loginTemplate = new ModelAndView("login");
                loginTemplate.addObject("code", "ALLO");
                return loginTemplate;
        }

        @PostMapping(path = "/login",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
        public ModelAndView login(AuthParamsDto params, final HttpServletRequest request) {

                User user = userRepository.findByEmail(params.getEmail());

                ModelAndView loginTemplate = new ModelAndView("login");
                loginTemplate.addObject("code", params.getCode());

                if(user == null){
                        loginTemplate.addObject("emailError", "User with this email was not found");
                        return loginTemplate;
                }
//                if(!b.matches(params.getPassword(), user.getPassword())){
//                        loginTemplate.addObject("passwordError", "Password Incorrect");
//                        return loginTemplate;
//                }

                UsernamePasswordAuthenticationToken authReq =
                        new UsernamePasswordAuthenticationToken(params.getEmail(), params.getPassword());
                Authentication auth = authManager.authenticate(authReq);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", sc);

                String externalUrl = "https://google.com";
                return new ModelAndView("redirect:" + externalUrl);

        }

}
