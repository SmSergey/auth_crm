package com.exceedit.auth.web.dto.users;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserParams {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String verificationCode;

    private String newEmail;

    private String surname;

    private String name;

    private String patronymic;

    private String fullName;

    private String permissions;

    private Boolean hasImage;

    private Boolean employeeId;
}
