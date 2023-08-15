package com.stage.elearning.repository;

import com.stage.elearning.model.affiche.Affiche;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AfficheRepository extends JpaRepository<Affiche ,Integer> {


    @Query(value = "SELECT A FROM Affiche A WHERE A.id = :afficheId")
    Optional<Affiche> fetchAfficheById(@Param("afficheId") final long afficheId);


    @Query(value = "SELECT A FROM Affiche A order by A.id")
    List<Affiche> fetchAllAffiche(Pageable pageable);

    @Query(value = "SELECT COUNT(A) FROM Affiche A")
    long getTotalAfficheCount();


    @Query(value = "SELECT A FROM Affiche A WHERE A.title LIKE :prefix%")
    List<Affiche> fetchAllAfficheWithPrefix(@Param("prefix")final String prefix);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Affiche  A WHERE A.id = :afficheId")
    void deleteAfficheById(@Param("afficheId") final long afficheId);
}
