package com.stage.elearning.service.token;

import com.stage.elearning.model.token.ConfirmationToken;
import com.stage.elearning.model.user.UserEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConfirmationTokenService {


    public ConfirmationToken fetchTokenByToken(final String token);
    public String generateConfirmationToken(@NotNull UserEntity userEntity);
    public void setConfirmedAt(final String token);
    public void saveConfirmationToken(@NotNull ConfirmationToken confirmationToken);

    public String getConfirmationPage();
    public String getAlreadyConfirmedPage();

}
