package com.exceedit.auth.model;



import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "user_code_xref")
@DynamicUpdate
public class UserCode {

    @Id
    @GeneratedValue()
    private Long id;


    @NotBlank
    private String userId;

    @NotBlank
    private String code;

}
