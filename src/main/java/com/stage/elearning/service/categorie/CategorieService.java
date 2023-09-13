package com.stage.elearning.service.categorie;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategorieService {
    public  ResponseEntity<CustomResponseEntity<CategorieDTO>> createCategorie(@NotNull final Categorie categorie);
    public  ResponseEntity<CustomResponseEntity<CategorieDTO>> fetchCategorieById(final long categorieId);
    public ResponseEntity<CustomResponseList<CategorieDTO>> fetchAllCategories(final long pageNumber);

    public ResponseEntity<CustomResponseEntity<List<CategorieDTO>>> searchCategories(final String prefix);
    public ResponseEntity<CustomResponseEntity<String>> modifyCategorieById(final long categorieId, @NotNull Categorie categorieDetails);
    public ResponseEntity<CustomResponseEntity<String>> deleteCategorieById(final long categorieId);

    public Categorie getCategorieById(final long categorieId);
    public Categorie getCategorieByName(final String categorieName);

}
