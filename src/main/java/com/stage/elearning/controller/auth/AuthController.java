package com.stage.elearning.controller.auth;


import com.stage.elearning.dto.auth.*;
import com.stage.elearning.service.auth.AuthService;
import com.stage.elearning.utility.CustomResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController (AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponseEntity<RegisterResponseDTO>>  register(@Valid @RequestBody RegisterDTO registerDto)
    {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponseEntity<LogInResponseDTO>>  login(@Valid @RequestBody LoginDTO loginDto)
    {
        return authService.login(loginDto);
    }

    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") final String token)
    {
        return authService.confirmToken(token);
    }

    @GetMapping("/refresh/{refreshToken}")
    public ResponseEntity<CustomResponseEntity<RefreshTokenResponseDTO>> renewAccessToken(@RequestParam("expiredToken") final String expiredToken,@PathVariable("refreshToken") final String refreshToken)
    {
        return authService.renewAccessToken(refreshToken,expiredToken);
    }

}