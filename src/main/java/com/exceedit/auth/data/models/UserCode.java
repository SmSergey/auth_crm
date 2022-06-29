package com.exceedit.auth.data.models;



import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user_code_xref")
@DynamicUpdate
public class UserCode {

    public UserCode() {
    }

    public UserCode(Long userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    @Id
    @GeneratedValue()
    private Long id;

    @Column(columnDefinition = "text", unique = true)
    @Getter
    private Long userId;

    @NotBlank
    @Getter
    @Column(columnDefinition = "text", unique = true)
    private String code;

}
