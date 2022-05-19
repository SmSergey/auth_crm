package com.exceedit.auth.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@TypeDef(typeClass = JsonType.class, name = "json")
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
    private String client_id;

    @NotBlank
    @Column(columnDefinition = "text", unique = true)
    private String client_secret;

    @NotBlank
    @Column(columnDefinition = "text")
    private String client_name;

    @Column(columnDefinition = "text")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
