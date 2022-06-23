package com.exceedit.auth.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTeamDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    private String owner;

    private String director;
}
