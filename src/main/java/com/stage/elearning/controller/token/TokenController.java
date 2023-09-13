package com.stage.elearning.controller.token;

import com.stage.elearning.service.token.TokenService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/token")
public class TokenController {

    private final TokenService tokenService;

    public TokenController (TokenService tokenService)
    {
        this.tokenService = tokenService;
    }

}
