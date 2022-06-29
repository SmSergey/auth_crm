package com.exceedit.auth.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateClientParams {
    @NotBlank
    private String name;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    private String description;

    private String[] allowedOrigin;

    private String[] redirectUris;
}
