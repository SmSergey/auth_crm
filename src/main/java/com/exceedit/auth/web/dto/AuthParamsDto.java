package com.exceedit.auth.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthParamsDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String code;

    @NotBlank
    private String clientId;

    @NotBlank
    private String redirectUrl;

    @NotBlank
    private String responseType;

    @NotBlank
    private String scope;


    AuthParamsDto() {
    }
}