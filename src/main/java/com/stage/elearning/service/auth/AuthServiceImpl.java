package com.stage.elearning.service.auth;


import com.stage.elearning.dto.auth.*;
import com.stage.elearning.dto.user.UserEntityDTOMapper;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.ConfirmationToken;
import com.stage.elearning.model.token.RefreshToken;
import com.stage.elearning.model.token.Token;
import com.stage.elearning.model.token.TokenType;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.security.jwt.JWTService;
import com.stage.elearning.service.email.EmailSenderService;
import com.stage.elearning.service.role.RoleService;
import com.stage.elearning.service.token.ConfirmationTokenService;
import com.stage.elearning.service.token.RefreshTokenService;
import com.stage.elearning.service.token.TokenService;
import com.stage.elearning.service.user.UserEntityService;
import com.stage.elearning.utility.CustomResponseEntity;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthServiceImpl  implements  AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserEntityService userEntityService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final UserEntityDTOMapper userEntityDTOMapper;
    private final RefreshTokenService refreshTokenService;


    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserEntityService userEntityService,
            RoleService roleService,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            TokenService tokenService,
            ConfirmationTokenService confirmationTokenService,
            EmailSenderService emailSenderService,
            UserEntityDTOMapper userEntityDTOMapper, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userEntityService = userEntityService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.userEntityDTOMapper = userEntityDTOMapper;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public ResponseEntity<CustomResponseEntity<RegisterResponseDTO>> register(@NotNull final RegisterDTO registerDto) {

        if (userEntityService.isFullNameRegistered(registerDto.getFullName())) {
            throw new IllegalArgumentException("Sorry, that username is already taken. Please choose a different one.");
        }

        if (userEntityService.isEmailRegistered(registerDto.getEmail())) {
            throw new IllegalArgumentException("Sorry, that email is already taken. Please choose a different one.");
        }

        if (userEntityService.isPhoneNumberRegistered(registerDto.getPhoneNumber())) {
            throw new IllegalArgumentException("Sorry, that phone number is already taken. Please choose a different one.");
        }

        Role role = roleService.fetchRoleByName("CLIENT");

        UserEntity user = new UserEntity();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail().toLowerCase());
        user.setAddress(registerDto.getAddress());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setCreatingDate(new Date());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(role);

        UserEntity savedUser = userEntityService.saveUser(user);

        String confirmationToken = confirmationTokenService.generateConfirmationToken(savedUser);
        String refreshToken = refreshTokenService.generateRefreshToken(savedUser);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken;
        emailSenderService.sendEmail(savedUser.getEmail(),"Confirmation email" , emailSenderService.emailTemplateConfirmation(savedUser.getFullName(),link));

        final RegisterResponseDTO registerResponseDTO = RegisterResponseDTO
                .builder()
                .confirmationToken(confirmationToken)
                .refreshToken(refreshToken)
                .userEntityDTO(userEntityDTOMapper.apply(savedUser))
                .build();
        final CustomResponseEntity<RegisterResponseDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK, registerResponseDTO);
        return new ResponseEntity<>(customResponseEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponseEntity<LogInResponseDTO>> login(@NotNull final LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userEntityService.getUserEntityByEmail(loginDto.getEmail());

        revokeAllUserAccessTokens(user);
        revokeAllUserRefreshToken(user);
        String jwtAccessToken = revokeGenerateAndSaveToken(user);
        String jwtRefreshToken = refreshTokenService.generateRefreshToken(user);


        final LogInResponseDTO logInResponseDto = LogInResponseDTO
                .builder()
                .userEntityDTO(userEntityDTOMapper.apply(user))
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
        final CustomResponseEntity<LogInResponseDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK, logInResponseDto);
        return new  ResponseEntity<>(customResponseEntity,HttpStatus.OK);
    }

    @Override
    @Transactional
    public String confirmToken(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenService.fetchTokenByToken(token);
        if(confirmationToken.getConfirmedAt() != null)
        {
            return confirmationTokenService.getAlreadyConfirmedPage();
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now()))
        {
            throw new  IllegalStateException("token expired.");
        }

        confirmationTokenService.setConfirmedAt(token);
        userEntityService.enableUserById(confirmationToken.getUserEntity().getId());
        return confirmationTokenService.getConfirmationPage();
    }

    public ResponseEntity<CustomResponseEntity<RefreshTokenResponseDTO>> renewAccessToken(final String refreshToken , final String expiredToken)
    {
        final Token currentToken = tokenService.getTokenByToken(expiredToken);
        final UserEntity currentUser = currentToken.getUserEntity();
        final RefreshToken currentRefreshToken = refreshTokenService.fetchRefreshTokenByToken(refreshToken);
        final boolean  isRefreshTokenValid = refreshTokenService.validateRefreshToken(refreshToken);
        if(currentRefreshToken.getUserEntity().getId() != currentUser.getId())
        {
            throw new IllegalStateException("The access token and refresh token u provided are not compatible.");
        }
        revokeAllUserAccessTokens(currentUser);
        String jwtAccessToken = revokeGenerateAndSaveToken(currentUser);
        final RefreshTokenResponseDTO refreshTokenResponseDTO = RefreshTokenResponseDTO
                .builder()
                .accessToken(jwtAccessToken)
                .refreshToken(refreshToken)
                .build();
        final CustomResponseEntity<RefreshTokenResponseDTO> customResponseEntity = new CustomResponseEntity<>(HttpStatus.OK,refreshTokenResponseDTO);
        return new ResponseEntity<>(customResponseEntity,HttpStatus.OK);

    }
    private String revokeGenerateAndSaveToken(UserEntity user) {
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserAccessTokens(user);
        saveUserAccessToken(user, jwtToken);

        return jwtToken;
    }

    private void saveUserAccessToken(@NotNull UserEntity userEntity, @NotNull String jwtToken) {
        var token = Token.builder()
                .userEntity(userEntity)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    private void revokeAllUserAccessTokens(@NotNull UserEntity userEntity) {
        var validUserTokens = tokenService.fetchAllValidTokenByUserId(userEntity.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }
    private void revokeAllUserRefreshToken(@NotNull UserEntity userEntity)
    {
        var validRefreshTokens = refreshTokenService.fetchAllRefreshTokenByUserId(userEntity.getId());
        if(validRefreshTokens.isEmpty())
        {
            return;
        }
        validRefreshTokens.forEach(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
        });
        refreshTokenService.saveAll(validRefreshTokens);
    }

}
