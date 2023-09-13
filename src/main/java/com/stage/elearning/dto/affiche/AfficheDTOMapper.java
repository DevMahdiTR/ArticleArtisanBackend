package com.stage.elearning.dto.affiche;

import com.stage.elearning.model.affiche.Affiche;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

 @Service
public class AfficheDTOMapper implements Function<Affiche, AfficheDTO> {

    @Override
    public AfficheDTO apply(@NotNull Affiche affiche) {
        return new AfficheDTO(
                affiche.getId(),
                affiche.getTitle(),
                affiche.getImage()
        );
    }
}
