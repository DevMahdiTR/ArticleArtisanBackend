package com.stage.elearning.dto.affiche;

import com.stage.elearning.model.file.FileData;

public record AfficheDTO(
        long id,
        String title,
        FileData image
) {
}
