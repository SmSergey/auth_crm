package com.exceedit.auth.data.models.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "teams")
@TypeDef(typeClass = JsonType.class, name = "json")
@DynamicUpdate
public class Team {
    @Id
    @GeneratedValue(generator = "team_generator")
    @SequenceGenerator(
            name = "team_generator",
            sequenceName = "team_sequence",
            initialValue = 10000
    )
    private Long id;

    @NotBlank
    @Setter
    @Column(columnDefinition = "text", unique = true)
    private String _id;

    @NotBlank
    @Column(columnDefinition = "text")
    private String address;

    @NotBlank
    @Column(columnDefinition = "text")
    private String name;

    @NotBlank
    @Column(columnDefinition = "text")
    private String city;

    @Column(columnDefinition = "text")
    private String owner; //TODO should be user id with link

    @Column(columnDefinition = "text")
    private String director; //TODO should be user id with link
}
