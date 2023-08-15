package com.stage.elearning.service.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.elearning.dto.affiche.AfficheDTO;
import com.stage.elearning.dto.article.ArticleDTO;
import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import com.stage.elearning.model.file.FileData;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ArticleService {
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> createArticle(final MultipartFile image, @NotNull final String jsonArticle , final long categorieId) throws IOException;
    public ResponseEntity<byte[]> fetchArticleImage(final long articleId) throws IOException;
    public ResponseEntity<CustomResponseEntity<ArticleDTO>> fetchArticleById(final long articleId);
    public ResponseEntity<CustomResponseList<ArticleDTO>>   fetchAllArticles(final long pageNumber);
    public ResponseEntity<CustomResponseEntity<String>> modifyArticleById(final MultipartFile image, final long articleId, @NotNull final String  jsonArticleDetails, final long categorieId)throws IOException;
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> searchArticle(  final String name,
                                                                                  final Boolean certified,
                                                                                  final Integer period,
                                                                                  final Integer price,
                                                                                  final String categorie,
                                                                                  final Date startingDate,
                                                                                  final Date endingDate);
    public ResponseEntity<CustomResponseEntity<String >> deleteArticleById(final long articleId) throws IOException;
    public ResponseEntity<CustomResponseEntity<String>> deleteArticleByName(final String articleName) throws IOException;
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryById(final long categorieId);
    public ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryByName(final String categorieName);

}
