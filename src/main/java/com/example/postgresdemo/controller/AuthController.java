package com.example.postgresdemo.controller;

import com.example.postgresdemo.model.User;
import com.example.postgresdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

class Test {
        private String email;
        private String password;
        private String code;

        Test(){

        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }
}

@RestController
@RequestMapping(path = "oauth")
public class AuthController {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private  static  final PasswordEncoder b = new BCryptPasswordEncoder();

        @GetMapping("/authorize")
        public ModelAndView authorize(Model model) {
                ModelAndView loginTemplate = new ModelAndView("login");
                loginTemplate.addObject("code", "ALLO");
                return loginTemplate;
        }

        @PostMapping(path = "/login",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
        public ModelAndView login(Test params) {

                User user = userRepository.findByEmail(params.getEmail());
                ModelAndView loginTemplate = new ModelAndView("login");
                loginTemplate.addObject("code", params.getCode());

                if(user == null){
                        loginTemplate.addObject("emailError", "User with this email was not found");
                        return loginTemplate;
                }
                if(!b.matches(params.getPassword(), user.getPassword())){
                        loginTemplate.addObject("passwordError", "Password Incorrect");
                        return loginTemplate;
                }

                String externalUrl = "https://google.com";
                return new ModelAndView("redirect:" + externalUrl);

        }

}
