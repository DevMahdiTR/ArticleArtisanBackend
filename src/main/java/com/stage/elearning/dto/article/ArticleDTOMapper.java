package com.stage.elearning.dto.article;

import com.stage.elearning.dto.categorie.CategorieDTO;
import com.stage.elearning.dto.categorie.CategorieDTOMapper;
import com.stage.elearning.model.acticle.Article;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ArticleDTOMapper implements Function<Article , ArticleDTO> {
    @Override
    public ArticleDTO apply(Article article) {
        return  new ArticleDTO(
                article.getId(),
                article.getName(),
                article.getStartingDate(),
                article.getEndingDate(),
                article.isCertification(),
                article.getTimePeriod(),
                article.getPrice(),
                new CategorieDTOMapper().apply(article.getCategorie()),
                article.getImage()
        );
    }
}
