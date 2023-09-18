package com.stage.elearning.dto.auth;

import com.stage.elearning.dto.user.UserEntityDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogInResponseDTO {
    private UserEntityDTO userEntityDTO;
    private String accessToken;
    private String refreshToken;
}
