package com.stage.elearning.repository;

import com.stage.elearning.model.file.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FileDataRepository extends JpaRepository<FileData,Integer> {


    @Query(value = "select fd from FileData fd where fd.id = :id")
    Optional<FileData> fetchFileDataById(long id);

    @Transactional
    @Modifying
    @Query(value = "delete from FileData f where f.id = :id")
    void deleteFileDataById(final long id);
}