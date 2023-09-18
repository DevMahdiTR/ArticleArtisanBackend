package com.stage.elearning.dto.article;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.chapter.ChapterDTO;
import com.stage.elearning.dto.user.UserEntityDTO;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.chapter.Chapter;
import com.stage.elearning.model.file.FileData;

import java.util.Date;
import java.util.List;

public record ArticleDTO (
        long id,
        String name,
        String description,
        Date statingDate,
        Date endingDate,
        List<ChapterDTO> chapterDTOList,
        List<UserEntityDTO> subscribedUsers,
        boolean certification,
        int timePeriod,
        float price,
        CategorieDTO categorieDTO,
        FileData image
){

}
