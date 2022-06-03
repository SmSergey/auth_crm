package com.exceedit.auth.web.dto;

import javax.validation.constraints.NotBlank;

public class CreateClientDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    private String description;

    private String[] allowedOrigin;

    private String[] redirectUris;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAllowedOrigin() {
        return allowedOrigin;
    }

    public void setAllowedOrigin(String[] allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    public String[] getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String[] redirectUris) {
        this.redirectUris = redirectUris;
    }
}
