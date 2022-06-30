package com.exceedit.auth.web.dto.oauth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OauthLoginParams {

    @NotBlank
    private String login_token;

    private Boolean rememberMe;

}
