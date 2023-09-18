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
    public String getConfirmationPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Confirmed</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #007bff;\n" +
                "            margin: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "        .container {\n" +
                "            text-align: center;\n" +
                "            padding: 30px;\n" +
                "            background-color: white;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-top: 10px;\n" +
                "        }\n" +
                "        .close-button {\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            padding: 10px 20px;\n" +
                "            border-radius: 5px;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 16px;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "        .close-button:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <h1>Email Confirmed</h1>\n" +
                "    <p>Your email address has been successfully confirmed. Thank you for joining us!</p>\n" +
                "    <button class=\"close-button\" onclick=\"window.close()\">Close</button>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @Override
    public String getAlreadyConfirmedPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Email Already Confirmed</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      background-color: #007bff;\n" +
                "      margin: 0;\n" +
                "      display: flex;\n" +
                "      justify-content: center;\n" +
                "      align-items: center;\n" +
                "      height: 100vh;\n" +
                "    }\n" +
                "    .container {\n" +
                "      text-align: center;\n" +
                "      padding: 30px;\n" +
                "      background-color: white;\n" +
                "      border-radius: 10px;\n" +
                "      box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);\n" +
                "    }\n" +
                "    h1 {\n" +
                "      color: #333;\n" +
                "    }\n" +
                "    p {\n" +
                "      color: #666;\n" +
                "      margin-top: 10px;\n" +
                "    }\n" +
                "    .close-button {\n" +
                "      background-color: #007bff;\n" +
                "      color: white;\n" +
                "      border: none;\n" +
                "      padding: 10px 20px;\n" +
                "      border-radius: 5px;\n" +
                "      cursor: pointer;\n" +
                "      font-size: 16px;\n" +
                "      transition: background-color 0.3s;\n" +
                "    }\n" +
                "    .close-button:hover {\n" +
                "      background-color: #0056b3;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h1>Email Already Confirmed</h1>\n" +
                "    <p>Your email address has already been confirmed. Thank you for your continued support!</p>\n" +
                "    <button class=\"close-button\" onclick=\"window.close()\">Close</button>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
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
                LocalDateTime.now().plusDays(10),
                userEntity
        );
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

}
