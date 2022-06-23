package com.exceedit.auth.web.dto;

import lombok.Data;

@Data
public class AuthParamsDto {

    private String email;
    private String password;
    private String code;
    private String clientId;
    private String redirectUrl;
    private String responseType;
    private String scope;
    private String clientSecret;

    AuthParamsDto() {
    }
}