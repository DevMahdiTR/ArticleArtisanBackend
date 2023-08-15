package com.stage.elearning.service.auth;

import com.stage.elearning.dto.auth.*;
import com.stage.elearning.utility.CustomResponseEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {


    public ResponseEntity<CustomResponseEntity<RegisterResponseDTO>> register(@NotNull final RegisterDTO registerDto) ;
    public ResponseEntity<CustomResponseEntity<LogInResponseDTO>>  login(@NotNull final LoginDTO loginDto);
    public ResponseEntity<CustomResponseEntity<String>> confirmToken(final String token);
    public ResponseEntity<CustomResponseEntity<RefreshTokenResponseDTO>> renewAccessToken(final String refreshToken, final String expiredToken);
}
