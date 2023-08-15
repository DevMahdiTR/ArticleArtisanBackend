package com.stage.elearning.dto.article;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;

import java.util.Date;

public record ArticleDTO (
        long id,
        String name,
        Date statingDate,
        Date endingDate,
        boolean certification,
        int timePeriod,
        float price,
        CategorieDTO categorieDTO,
        FileData image
){

}
