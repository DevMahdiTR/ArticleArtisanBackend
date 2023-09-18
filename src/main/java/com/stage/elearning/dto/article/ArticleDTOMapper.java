package com.stage.elearning.dto.article;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.categorie.CategorieDTOMapper;
import com.stage.elearning.dto.chapter.ChapterDTO;
import com.stage.elearning.dto.chapter.ChapterDTOMapper;
import com.stage.elearning.dto.user.UserEntityDTOMapper;
import com.stage.elearning.model.acticle.Article;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class ArticleDTOMapper implements Function<Article , ArticleDTO> {
    @Override
    public ArticleDTO apply(Article article) {
        return  new ArticleDTO(
                article.getId(),
                article.getName(),
                article.getDescription(),
                article.getStartingDate(),
                article.getEndingDate(),
                article.getChapters().stream().map(chapter -> new ChapterDTOMapper().apply(chapter)).toList(),
                article.getSubscribers().stream().map(userEntity -> new UserEntityDTOMapper().apply(userEntity)).toList(),
                article.isCertification(),
                article.getTimePeriod(),
                article.getPrice(),
                new CategorieDTOMapper().apply(article.getCategorie()),
                article.getImage()
        );
    }
}
