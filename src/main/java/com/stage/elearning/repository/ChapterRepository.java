package com.stage.elearning.repository;

import com.stage.elearning.model.chapter.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {


    @Query(value = "SELECT C FROM Chapter  C WHERE C.id = :chapterId")
    Optional<Chapter> fetchChapterById(@Param("chapterId") final long chapterId);


    @Transactional
    @Modifying
    @Query(value = "DELETE  FROM Chapter  C WHERE  C.id = :chapterId")
    void deleteChapterById(@Param("chapterId") final long chapterId);

}
