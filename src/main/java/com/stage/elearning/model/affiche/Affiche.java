package com.stage.elearning.model.affiche;

import com.stage.elearning.model.file.FileData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "affiches")
public class Affiche {

    @SequenceGenerator(
            name = "affiche_sequence",
            sequenceName = "affiche_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "affiche_sequence"
    )
    @Column(name ="id", unique = true, nullable = false)
    private long id;


    @Column(name = "title",unique = true,nullable = false)
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private FileData image;
}
