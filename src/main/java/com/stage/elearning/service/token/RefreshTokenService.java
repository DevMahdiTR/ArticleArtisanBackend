package com.stage.elearning.service.token;

import com.stage.elearning.model.token.RefreshToken;
import com.stage.elearning.model.user.UserEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface RefreshTokenService {

    public String generateRefreshToken(@NotNull final UserEntity userEntity);
    public List<RefreshToken> fetchAllRefreshTokenByUserId(final UUID userId);
    public RefreshToken fetchRefreshTokenByToken(final String refreshToken);
    public boolean validateRefreshToken(final String refreshToken);
    public void saveAll(List<RefreshToken> refreshTokenList);
}
