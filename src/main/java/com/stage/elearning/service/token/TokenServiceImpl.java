package com.stage.elearning.service.token;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.token.Token;
import com.stage.elearning.repository.TokenRepository;
import com.stage.elearning.security.jwt.JWTService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository, JWTService jwtService)
    {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }


    @Override
    public Token getTokenByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(
                ()-> new ResourceNotFoundException("The token u provided could not be found in our system")
        );
    }

    @Override
    public List<Token> fetchAllValidTokenByUserId(UUID userId) {
        return tokenRepository.fetchAllValidTokenByUserId(userId);
    }

    @Override
    public Token save(@NotNull Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public List<Token> saveAll(List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }
}
