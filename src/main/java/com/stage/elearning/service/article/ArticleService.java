package com.stage.elearning.service.article;

import com.stage.elearning.dto.article.ArticleDTO;
import com.stage.elearning.dto.chapter.ChapterDTO;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ArticleService {
    ResponseEntity<CustomResponseEntity<ArticleDTO>> createArticle(final MultipartFile image, @NotNull final String jsonArticle , final long categorieId) throws IOException;
    ResponseEntity<byte[]> fetchArticleImage(final long articleId) throws IOException;
    ResponseEntity<CustomResponseEntity<ArticleDTO>> fetchArticleById(final long articleId);
    ResponseEntity<CustomResponseList<ArticleDTO>>   fetchAllArticles(final long pageNumber);
    ResponseEntity<CustomResponseEntity<String>> modifyArticleById(final MultipartFile image, final long articleId, @NotNull final String  jsonArticleDetails, final long categorieId)throws IOException;
    ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> searchArticle(  final String name,
                                                                                  final Boolean certified,
                                                                                  final Integer period,
                                                                                  final Integer price,
                                                                                  final String categorie,
                                                                                  final Date startingDate,
                                                                                  final Date endingDate);
    ResponseEntity<CustomResponseEntity<String >> deleteArticleById(final long articleId) throws IOException;
    ResponseEntity<CustomResponseEntity<String>> deleteArticleByName(final String articleName) throws IOException;
    ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryById(final long categorieId);ResponseEntity<CustomResponseEntity<List<ArticleDTO>>> fetchArticlesByCategoryByName(final String categorieName);
    ResponseEntity<CustomResponseEntity<String>> subscribeToArticle(@NotNull UserDetails userDetails, final long articleId);
    ResponseEntity<CustomResponseEntity<String>> unsubscribeToArticle(@NotNull UserDetails userDetails, final long articleId);

}
