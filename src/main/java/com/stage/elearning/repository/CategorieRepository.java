package com.stage.elearning.repository;

import com.stage.elearning.model.categories.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CategorieRepository  extends JpaRepository<Categorie, Integer> {


    @Query(value = "SELECT C FROM Categorie C WHERE C.id = :categorieId")
    Optional<Categorie> fetchCategorieById(@Param("categorieId") final long categorieId);

    @Query(value = "SELECT C FROM Categorie C WHERE C.name = :categorieName")
    Optional<Categorie> fetchCategorieByName(@Param("categorieName") final String categorieName);

    @Query(value = "SELECT COUNT(C) from Categorie C ")
    long getTotalCategoriesCount();
    @Query(value = "SELECT C FROM Categorie  C WHERE LOWER(C.name) LIKE LOWER(CONCAT('%',:prefix,'%')) OR (LOWER(C.description) LIKE LOWER(CONCAT('%',:prefix,'%')) ) ")
    List<Categorie> searchCategorie(@Param("prefix") final String prefix);
    @Query(value = "SELECT C FROM Categorie C ORDER BY C.id")
    List<Categorie> fetchAllCategories(Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE Categorie C WHERE C.id = :categorieId")
    void deleteCategorieById(@Param("categorieId") final long categorieId);

}
