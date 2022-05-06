package com.example.postgresdemo.controller;

import com.example.postgresdemo.model.User;
import com.example.postgresdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.MultivaluedMap;

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
                ModelAndView mav = new ModelAndView("login");
                mav.addObject("code", "ALLO");
                mav.addObject("passwordError", "ALLO");
                return mav;
        }

        @PostMapping(path = "/login",
                consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
        public String login(Test params) {

                User user = userRepository.findByEmail(params.getEmail());
                if(user == null){
                        return "Not found";
                }
                if(!b.matches(params.getPassword(), user.getPassword())){
                        return "Invalid password";
                }
                System.out.println("LOOOG" + user.getId());
                System.out.println("LOOOG" + params.getEmail());
                System.out.println("LOOOG" + params.getPassword());
                return "Auhtorized";
        }
}
