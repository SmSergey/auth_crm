package com.exceedit.auth.data.models.entities;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "clients")
@DynamicUpdate
public class Client {
    @Id
    @GeneratedValue(generator = "client_generator")
    @SequenceGenerator(
            name = "client_generator",
            sequenceName = "client_sequence",
            initialValue = 1000
    )
    private Long id;

    @NotBlank
    @Column(columnDefinition = "text", unique = true)
    private String clientId;

    @NotBlank
    @Column(columnDefinition = "text", unique = true)
    private String clientSecret;

    @NotBlank
    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "varchar[]")
    private String[] allowedOrigin;

    @Column(columnDefinition = "varchar[]")
    private String[] redirectUris;


    public boolean validateSecret(@NotNull String secret) {
        return this.getClientSecret().equals(secret);
    }
}
