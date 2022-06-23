package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.web.dto.AuthParamsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "api/v1")
public class TokenController {


    @GetMapping(path = "tokens")
    public void getTokens(AuthParamsDto params) {

    }
}
