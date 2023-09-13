package com.stage.elearning.controller.categorie;


import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.service.categorie.CategorieService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categorie")
public class CategorieController {


    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @PostMapping("admin/add")
    public  ResponseEntity<CustomResponseEntity<CategorieDTO>> createCategorie(@Valid @RequestBody Categorie categorie)
    {
        return categorieService.createCategorie(categorie);
    }

    @GetMapping("/all/get/id/{categorieId}")
    public ResponseEntity<CustomResponseEntity<CategorieDTO>> fetchCategorieById(@PathVariable("categorieId") final long categorieId) {
        return categorieService.fetchCategorieById(categorieId);
    }

    @GetMapping("/all/get/all_categories")
    public ResponseEntity<CustomResponseList<CategorieDTO>> fetchAllCategories(
            @RequestParam(value = "pageNumber" ,required = true) final long pageNumber
            ) {
        return categorieService.fetchAllCategories(pageNumber);
    }
    @GetMapping("/all/get/prefix/{prefix}")
    public ResponseEntity<CustomResponseEntity<List<CategorieDTO>>> searchCategories(@PathVariable("prefix")  final String prefix) {
        return categorieService.searchCategories(prefix);
    }

        @PutMapping("/admin/update/id/{categorieId}")
    public ResponseEntity<CustomResponseEntity<String>> modifyCategorieById(@PathVariable("categorieId") final long categorieId , @Valid @RequestBody final Categorie categorie)
    {
        return categorieService.modifyCategorieById(categorieId,categorie);
    }

    @DeleteMapping("/admin/delete/id/{categorieId}")
    public ResponseEntity<CustomResponseEntity<String>> deleteCategorieById(@PathVariable("categorieId") final long categorieId)
    {
        return categorieService.deleteCategorieById(categorieId);
    }

}

