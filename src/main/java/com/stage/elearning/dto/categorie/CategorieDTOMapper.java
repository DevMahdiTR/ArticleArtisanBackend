package com.stage.elearning.dto.categorie;

import com.stage.elearning.model.categories.Categorie;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategorieDTOMapper implements Function<Categorie , CategorieDTO> {
    @Override
    public CategorieDTO apply(@NotNull Categorie categorie) {
        return new CategorieDTO(
                categorie.getId(),
                categorie.getName(),
                categorie.getDescription()
        );
    }
}
