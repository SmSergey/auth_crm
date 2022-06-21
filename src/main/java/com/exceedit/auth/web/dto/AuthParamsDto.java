package com.exceedit.auth.web.dto;

import lombok.Data;

@Data
public class AuthParamsDto {
    private String email;
    private String password;
    private String code;

    private String clientId;

    private String clientSecret;

    AuthParamsDto() {
    }
}