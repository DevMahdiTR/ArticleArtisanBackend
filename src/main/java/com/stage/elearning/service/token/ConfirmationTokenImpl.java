package com.stage.elearning.service.token;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.token.ConfirmationToken;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.ConfirmationTokenRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenImpl  implements  ConfirmationTokenService{

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenImpl(ConfirmationTokenRepository confirmationTokenRepository)
    {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public void saveConfirmationToken(@NotNull ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken fetchTokenByToken(String token) {
        return confirmationTokenRepository.fetchConfirmationTokenByToken(token).orElseThrow(
                ()-> new ResourceNotFoundException("This Token could not be found in our system.")
        );
    }

    @Override
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = fetchTokenByToken(token);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public String generateConfirmationToken(@NotNull UserEntity userEntity) {

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }
}
