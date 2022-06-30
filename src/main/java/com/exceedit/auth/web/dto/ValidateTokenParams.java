package com.exceedit.auth.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ValidateTokenParams {

    @NotEmpty(message = "access token is required")
    private String accessToken;
}
