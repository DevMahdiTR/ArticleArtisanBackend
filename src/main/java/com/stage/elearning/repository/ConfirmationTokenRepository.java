package com.stage.elearning.repository;

import com.stage.elearning.model.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken , Integer> {


    @Query(value =  "SELECT CT FROM ConfirmationToken CT WHERE  CT.token = :token")
    Optional<ConfirmationToken> fetchConfirmationTokenByToken(@Param("token") final String token);
}
