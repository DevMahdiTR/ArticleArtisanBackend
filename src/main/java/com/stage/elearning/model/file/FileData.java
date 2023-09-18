package com.stage.elearning.model.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Entity
@Table(name = "files")
public class FileData {
    @SequenceGenerator(
            name = "file_sequence",
            sequenceName = "file_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_sequence"
    )
    @Column(name = "id")
    private long id;
    @Column(name ="name")
    private String name;
    @Column(name ="type")
    private String type;
    @Column(name ="file_path")
    private String filePath;
}