package com.stage.elearning.service.token;

import com.stage.elearning.model.token.Token;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface TokenService {



    public Token getTokenByToken(final String token);
    public List<Token> fetchAllValidTokenByUserId(final UUID userId);
    public Token save(@NotNull final Token token);
    public List<Token> saveAll(List<Token> tokens);

}
