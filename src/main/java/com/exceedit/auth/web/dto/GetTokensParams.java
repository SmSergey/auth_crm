package com.exceedit.auth.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetTokensParams {

    @NotBlank
    public String redirectUrl;

    @NotBlank
    public String code;

}
