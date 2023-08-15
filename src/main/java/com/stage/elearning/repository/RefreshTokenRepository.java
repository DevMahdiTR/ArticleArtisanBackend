package com.stage.elearning.repository;


import com.stage.elearning.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken , Integer> {


    @Query(value = "SELECT RT FROM RefreshToken RT WHERE RT.refreshToken = :refreshToken")
    Optional<RefreshToken> fetchByToken(@Param("refreshToken")final String refreshToken);

    @Query(value = "SELECT RT FROM RefreshToken RT WHERE RT.userEntity.id = :userId")
    List<RefreshToken> fetchAllRefreshTokenByUserId(@Param("userId")final UUID userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM RefreshToken RT WHERE RT.id = :refreshTokenId")
    void deleteRefreshTokenById(@Param("refreshTokenId")final long id);
}
