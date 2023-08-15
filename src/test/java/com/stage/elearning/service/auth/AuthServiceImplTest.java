package com.stage.elearning.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.dto.auth.LogInResponseDTO;
import com.stage.elearning.dto.auth.LoginDTO;
import com.stage.elearning.dto.auth.RegisterDTO;
import com.stage.elearning.dto.user.UserEntityDTO;
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

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthServiceImplTest {
    @Autowired
    private AuthServiceImpl authServiceImpl;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private JWTService jWTService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserEntityDTOMapper userEntityDTOMapper;

    @MockBean
    private UserEntityService userEntityService;

    @Test
    void testRegister() {
        when(userEntityService.isFullNameRegistered(Mockito.<String>any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl
                .register(new RegisterDTO("Dr Jane Doe", "jane.doe@example.org", "42 Main St", "6625550144", "iloveyou")));
        verify(userEntityService).isFullNameRegistered(Mockito.<String>any());
    }

    @Test
    void testRegister2() {
        when(userEntityService.isEmailRegistered(Mockito.<String>any())).thenReturn(true);
        when(userEntityService.isFullNameRegistered(Mockito.<String>any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl
                .register(new RegisterDTO("Dr Jane Doe", "jane.doe@example.org", "42 Main St", "6625550144", "iloveyou")));
        verify(userEntityService).isEmailRegistered(Mockito.<String>any());
        verify(userEntityService).isFullNameRegistered(Mockito.<String>any());
    }


    @Test
    void testRegister3() {
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.register(null));
    }

    @Test
    void testLogin() throws AuthenticationException {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);
        when(userEntityService.getUserEntityByEmail(Mockito.<String>any())).thenReturn(userEntity);
        when(jWTService.generateToken(Mockito.<UserEntity>any())).thenReturn("ABC123");

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("42 Main St");
        userEntity2
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setEnabled(true);
        userEntity2.setFullName("Dr Jane Doe");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("iloveyou");
        userEntity2.setPhoneNumber("6625550144");
        userEntity2.setRole(role2);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokenType(TokenType.BEARER);
        token.setUserEntity(userEntity2);
        when(tokenService.save(Mockito.<Token>any())).thenReturn(token);
        when(tokenService.fetchAllValidTokenByUserId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        UUID id = UUID.randomUUID();
        Date creationDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UserEntityDTO userEntityDTO = new UserEntityDTO(id, "Dr Jane Doe", "jane.doe@example.org", "42 Main St",
                "6625550144", creationDate, true, new Role());

        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any())).thenReturn(userEntityDTO);
        when(refreshTokenService.generateRefreshToken(Mockito.<UserEntity>any())).thenReturn("ABC123");
        when(refreshTokenService.fetchAllRefreshTokenByUserId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        ResponseEntity<CustomResponseEntity<LogInResponseDTO>> actualLoginResult = authServiceImpl.login(new LoginDTO());
        assertTrue(actualLoginResult.hasBody());
        assertTrue(actualLoginResult.getHeaders().isEmpty());
        assertEquals(200, actualLoginResult.getStatusCodeValue());
        CustomResponseEntity<LogInResponseDTO> body = actualLoginResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        LogInResponseDTO data = body.getData();
        assertEquals("ABC123", data.getAccessToken());
        assertSame(userEntityDTO, data.getUserEntityDTO());
        assertEquals("ABC123", data.getRefreshToken());
        verify(userEntityService).getUserEntityByEmail(Mockito.<String>any());
        verify(jWTService).generateToken(Mockito.<UserEntity>any());
        verify(tokenService).save(Mockito.<Token>any());
        verify(tokenService, atLeast(1)).fetchAllValidTokenByUserId(Mockito.<UUID>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
        verify(refreshTokenService).generateRefreshToken(Mockito.<UserEntity>any());
        verify(refreshTokenService).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    @Test
    void testLogin2() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(new LoginDTO()));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    @Test
    void testLogin3() throws AuthenticationException {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);
        when(userEntityService.getUserEntityByEmail(Mockito.<String>any())).thenReturn(userEntity);
        when(jWTService.generateToken(Mockito.<UserEntity>any())).thenReturn(null);
        when(tokenService.fetchAllValidTokenByUserId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        when(refreshTokenService.fetchAllRefreshTokenByUserId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(new LoginDTO()));
        verify(userEntityService).getUserEntityByEmail(Mockito.<String>any());
        verify(jWTService).generateToken(Mockito.<UserEntity>any());
        verify(tokenService, atLeast(1)).fetchAllValidTokenByUserId(Mockito.<UUID>any());
        verify(refreshTokenService).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    @Test
    void testLogin4() throws AuthenticationException {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);
        when(userEntityService.getUserEntityByEmail(Mockito.<String>any())).thenReturn(userEntity);
        when(jWTService.generateToken(Mockito.<UserEntity>any())).thenReturn("ABC123");

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("42 Main St");
        userEntity2
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setEnabled(true);
        userEntity2.setFullName("Dr Jane Doe");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("iloveyou");
        userEntity2.setPhoneNumber("6625550144");
        userEntity2.setRole(role2);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokenType(TokenType.BEARER);
        token.setUserEntity(userEntity2);

        Role role3 = new Role();
        role3.setId(1L);
        role3.setName("Name");

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setAddress("42 Main St");
        userEntity3
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setEnabled(true);
        userEntity3.setFullName("Dr Jane Doe");
        userEntity3.setId(UUID.randomUUID());
        userEntity3.setPassword("iloveyou");
        userEntity3.setPhoneNumber("6625550144");
        userEntity3.setRole(role3);

        Token token2 = new Token();
        token2.setExpired(true);
        token2.setId(1L);
        token2.setRevoked(true);
        token2.setToken("ABC123");
        token2.setTokenType(TokenType.BEARER);
        token2.setUserEntity(userEntity3);

        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(token2);
        when(tokenService.saveAll(Mockito.<List<Token>>any())).thenReturn(new ArrayList<>());
        when(tokenService.save(Mockito.<Token>any())).thenReturn(token);
        when(tokenService.fetchAllValidTokenByUserId(Mockito.<UUID>any())).thenReturn(tokenList);
        UUID id = UUID.randomUUID();
        Date creationDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UserEntityDTO userEntityDTO = new UserEntityDTO(id, "Dr Jane Doe", "jane.doe@example.org", "42 Main St",
                "6625550144", creationDate, true, new Role());

        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any())).thenReturn(userEntityDTO);
        when(refreshTokenService.generateRefreshToken(Mockito.<UserEntity>any())).thenReturn("ABC123");
        when(refreshTokenService.fetchAllRefreshTokenByUserId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        ResponseEntity<CustomResponseEntity<LogInResponseDTO>> actualLoginResult = authServiceImpl.login(new LoginDTO());
        assertTrue(actualLoginResult.hasBody());
        assertTrue(actualLoginResult.getHeaders().isEmpty());
        assertEquals(200, actualLoginResult.getStatusCodeValue());
        CustomResponseEntity<LogInResponseDTO> body = actualLoginResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        LogInResponseDTO data = body.getData();
        assertEquals("ABC123", data.getAccessToken());
        assertSame(userEntityDTO, data.getUserEntityDTO());
        assertEquals("ABC123", data.getRefreshToken());
        verify(userEntityService).getUserEntityByEmail(Mockito.<String>any());
        verify(jWTService).generateToken(Mockito.<UserEntity>any());
        verify(tokenService).save(Mockito.<Token>any());
        verify(tokenService, atLeast(1)).fetchAllValidTokenByUserId(Mockito.<UUID>any());
        verify(tokenService, atLeast(1)).saveAll(Mockito.<List<Token>>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
        verify(refreshTokenService).generateRefreshToken(Mockito.<UserEntity>any());
        verify(refreshTokenService).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    @Test
    void testConfirmToken() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken.setExpiresAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken.setId(1L);
        confirmationToken.setToken("ABC123");
        confirmationToken.setUserEntity(userEntity);
        when(confirmationTokenService.fetchTokenByToken(Mockito.<String>any())).thenReturn(confirmationToken);
        assertThrows(IllegalStateException.class, () -> authServiceImpl.confirmToken("ABC123"));
        verify(confirmationTokenService).fetchTokenByToken(Mockito.<String>any());
    }

    @Test
    void testRenewAccessToken() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokenType(TokenType.BEARER);
        token.setUserEntity(userEntity);
        when(tokenService.getTokenByToken(Mockito.<String>any())).thenReturn(token);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("42 Main St");
        userEntity2
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setEnabled(true);
        userEntity2.setFullName("Dr Jane Doe");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("iloveyou");
        userEntity2.setPhoneNumber("6625550144");
        userEntity2.setRole(role2);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity2);
        when(refreshTokenService.validateRefreshToken(Mockito.<String>any())).thenReturn(true);
        when(refreshTokenService.fetchRefreshTokenByToken(Mockito.<String>any())).thenReturn(refreshToken);
        assertThrows(IllegalStateException.class, () -> authServiceImpl.renewAccessToken("ABC123", "ABC123"));
        verify(tokenService).getTokenByToken(Mockito.<String>any());
        verify(refreshTokenService).validateRefreshToken(Mockito.<String>any());
        verify(refreshTokenService).fetchRefreshTokenByToken(Mockito.<String>any());
    }

    @Test
    void testRenewAccessToken2() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokenType(TokenType.BEARER);
        token.setUserEntity(userEntity);
        when(tokenService.getTokenByToken(Mockito.<String>any())).thenReturn(token);
        when(refreshTokenService.fetchRefreshTokenByToken(Mockito.<String>any()))
                .thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> authServiceImpl.renewAccessToken("ABC123", "ABC123"));
        verify(tokenService).getTokenByToken(Mockito.<String>any());
        verify(refreshTokenService).fetchRefreshTokenByToken(Mockito.<String>any());
    }

    @Test
    void testRenewAccessToken3() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        userEntity.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("42 Main St");
        userEntity2
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setEnabled(true);
        userEntity2.setFullName("Dr Jane Doe");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("iloveyou");
        userEntity2.setPhoneNumber("6625550144");
        userEntity2.setRole(role2);
        Token token = mock(Token.class);
        when(token.getUserEntity()).thenReturn(userEntity2);
        doNothing().when(token).setExpired(anyBoolean());
        doNothing().when(token).setId(anyLong());
        doNothing().when(token).setRevoked(anyBoolean());
        doNothing().when(token).setToken(Mockito.<String>any());
        doNothing().when(token).setTokenType(Mockito.<TokenType>any());
        doNothing().when(token).setUserEntity(Mockito.<UserEntity>any());
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokenType(TokenType.BEARER);
        token.setUserEntity(userEntity);
        when(tokenService.getTokenByToken(Mockito.<String>any())).thenReturn(token);

        Role role3 = new Role();
        role3.setId(1L);
        role3.setName("Name");

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setAddress("42 Main St");
        userEntity3
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setEnabled(true);
        userEntity3.setFullName("Dr Jane Doe");
        userEntity3.setId(UUID.randomUUID());
        userEntity3.setPassword("iloveyou");
        userEntity3.setPhoneNumber("6625550144");
        userEntity3.setRole(role3);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity3);
        when(refreshTokenService.validateRefreshToken(Mockito.<String>any())).thenReturn(true);
        when(refreshTokenService.fetchRefreshTokenByToken(Mockito.<String>any())).thenReturn(refreshToken);
        assertThrows(IllegalStateException.class, () -> authServiceImpl.renewAccessToken("ABC123", "ABC123"));
        verify(tokenService).getTokenByToken(Mockito.<String>any());
        verify(token).getUserEntity();
        verify(token).setExpired(anyBoolean());
        verify(token).setId(anyLong());
        verify(token).setRevoked(anyBoolean());
        verify(token).setToken(Mockito.<String>any());
        verify(token).setTokenType(Mockito.<TokenType>any());
        verify(token).setUserEntity(Mockito.<UserEntity>any());
        verify(refreshTokenService).validateRefreshToken(Mockito.<String>any());
        verify(refreshTokenService).fetchRefreshTokenByToken(Mockito.<String>any());
    }
}

