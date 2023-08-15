package com.stage.elearning.service.auth;

import com.stage.elearning.repository.RefreshTokenRepository;
import com.stage.elearning.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){ return;}
        final String jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if(storedToken != null) {
            var refreshToken = refreshTokenRepository.fetchAllRefreshTokenByUserId(storedToken.getUserEntity().getId());
            var lastValidRefreshToken = refreshToken.get(refreshToken.size()-1);
            lastValidRefreshToken.setExpired(true);
            lastValidRefreshToken.setRevoked(true);
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            refreshTokenRepository.save(lastValidRefreshToken);
        }

    }
}
