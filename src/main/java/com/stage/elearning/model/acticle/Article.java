package com.stage.elearning.model.acticle;


import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.chapter.Chapter;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.model.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
public class Article {


    @SequenceGenerator(
            name = "article_sequence",
            sequenceName = "article_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "article_sequence"
    )
    @Column(name = "id" , unique = true ,nullable = false)
    private long Id;


    @Column(name = "name" , unique = true , nullable = false)
    private String name ;

    @Column(name  = "description" , nullable = false)
    @Size(min = 3,max = 999999999)
    private String description;

    @Column(name ="price", nullable = false)
    @Min(value = 0, message = "Price cannot be less than 0")
    private float price;

    @Column(name = "starting_date", nullable = false)
    private Date startingDate;

    @Column(name = "ending_date", nullable = false)
    private Date endingDate;

    @Column(name = "certification", nullable = false)
    private boolean certification;

    @Column(name = "time_period", nullable = false)
    @Min(value = 0, message = "period cannot be less than 0")
    private int timePeriod;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private FileData image;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "user_article_subscription",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_entity_id")
    )
    private List<UserEntity> subscribers = new ArrayList<>();

}
