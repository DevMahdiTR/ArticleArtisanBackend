package com.stage.elearning.repository;

import com.stage.elearning.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@Transactional(readOnly = true)
public interface TokenRepository  extends JpaRepository<Token,Integer> {

    @Query(value = "select t from Token t inner join UserEntity u on t.userEntity.id = u.id where u.id = :userId and (t.expired = false or t.revoked = false)")
    List<Token> fetchAllValidTokenByUserId(@Param("userId") UUID userId);

    @Query(value = "select t from Token t where t.token = :token")
    Optional<Token> findByToken(@Param("token") String token);

    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Token t WHERE t.token = :token AND t.expired = false AND t.revoked = false")
    boolean isTokenValidAndExist(@Param("token") final String token);


}
