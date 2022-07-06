package com.exceedit.auth.data.models.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
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
    @Setter
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

    @Column(columnDefinition = "text")
    private String city;

    @Column(columnDefinition = "text")
    private String owner;

    @Column(columnDefinition = "text")
    private String director;

    @Column(columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "text")
    private String employee;

    @Column(columnDefinition = "text")
    private String team;




    public static class UserPrincipal implements UserDetails {

        private User user;

        public UserPrincipal(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return this.user.getPassword();
        }

        @Override
        public String getUsername() {
            return this.user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }
}
