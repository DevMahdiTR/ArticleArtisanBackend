package com.stage.elearning.repository;

import com.stage.elearning.model.acticle.Article;
import com.stage.elearning.model.categories.Categorie;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ArticleRepository  extends JpaRepository<Article , Integer> {


    @Query(value = "SELECT A FROM Article A WHERE A.Id = :articleId")
    Optional<Article> fetchArticleById(@Param("articleId") final long articleId);
    @Query(value = "SELECT A FROM Article A WHERE A.name = :articleName")
    Optional<Article> fetchArticleByName(@Param("articleName") final String articleName);

    @Query(value = "SELECT A FROM Article A ORDER BY A.Id")
    List<Article> fetchAllArticles(Pageable pageable);

    @Query("SELECT A FROM Article A " +
            "WHERE COALESCE(:name , A.name) = A.name " +
            "AND COALESCE(:certified , A.certification) = A.certification " +
            "AND COALESCE(:period , A.timePeriod) = A.timePeriod " +
            "AND COALESCE(:price , A.price)  = A.price " +
            "AND COALESCE(:categorie, A.categorie.name) = A.categorie.name " +
            "AND COALESCE(:startingDate , A.startingDate) <= A.startingDate " +
            "AND COALESCE(:endingDate,A.endingDate) >= A.endingDate ")
    List<Article> searchArticle(
            @Param("name") final String name,
            @Param("certified") final Boolean certified,
            @Param("period") final Integer period,
            @Param("price") final Integer price,
            @Param("categorie") final String categorie,
            @Param("startingDate") final Date startingDate,
            @Param("endingDate") final Date endingDate
    );

    @Query(value = "SELECT COUNT(A) from Article A")
    long getTotalArticleCount();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Article A WHERE A.name = :articleName")
    void deleteArticleByName(@Param("articleName") final String articleName);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Article A WHERE A.Id = :articleId")
    void deleteArticleById(@Param("articleId") final long articleId);

}
