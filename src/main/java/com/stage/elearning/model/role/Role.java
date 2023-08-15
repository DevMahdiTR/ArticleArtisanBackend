package com.stage.elearning.model.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Entity
@Table(name = "roles")
public class Role {

    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    @Column(name ="id", unique = true)
    private long id;
    @Column(name ="name" ,unique = true, nullable = false)
    @NotBlank
    private String name;

    public Role() {}
    public Role(String name)
    {
        this.name = name;
    }
}
