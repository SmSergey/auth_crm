package com.exceedit.auth.data.models;

import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "user_tokens_xref")
@DynamicUpdate
public class UserTokens {

    public UserTokens() {

    }

    public UserTokens(Long userId, String accessToken, String refreshToken){
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Id
    @GeneratedValue()
    private Long id;

    @Column(columnDefinition = "text", unique = true)
    @Getter
    private Long userId;

    @Column(columnDefinition = "text", unique = true)
    @Getter
    private String accessToken;

    @Column(columnDefinition = "text", unique = true)
    @Getter
    private String refreshToken;
}
