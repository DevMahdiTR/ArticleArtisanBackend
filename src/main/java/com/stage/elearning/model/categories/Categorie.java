package com.stage.elearning.model.categories;


import com.stage.elearning.model.acticle.Article;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Categorie {

    @SequenceGenerator(
            name = "categorie_sequence",
            sequenceName = "categorie_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "categorie_sequence"
    )
    @Column(name = "id" , unique = true , nullable = false)
    private long id;

    @Column(name = "name", unique = true , nullable = false)
    private String name;

    @Column(name  = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

}
