package com.example.postgresdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.LinkedHashMap;

@Entity
@Table(name = "users")
@TypeDef(typeClass = JsonType.class, name = "json")
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(generator = "user_generator")
    @SequenceGenerator(
            name = "user_generator",
            sequenceName = "user_sequence",
            initialValue = 1000
    )
    private Long id;
    @NotBlank
    @Column(columnDefinition = "text", unique = true)
    private String _id;

    @NotBlank
    @Column(columnDefinition = "text", unique = true)
    private String email;

    @NotBlank
    @Column(columnDefinition = "text")
    private String password;

    @Column(columnDefinition = "text")
    private String verificationCode;

    @Column(columnDefinition = "text")
    private String newEmail;

    @Column(columnDefinition = "text")
    private String surname;

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String patronymic;


    @Column(columnDefinition = "text")
    private String fullName;


    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
        private String permissions;


    @Column(columnDefinition = "boolean default false")
    private Boolean hasImage;


    @Column(columnDefinition = "text")
    private Boolean employeeId;


    @Column(columnDefinition = "timestamp")
    @CreationTimestamp
    private Date createdAt;


    @Column(columnDefinition = "timestamp")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @JsonIgnore
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Boolean employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
