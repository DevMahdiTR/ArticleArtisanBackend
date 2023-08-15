package com.stage.elearning.controller.auth;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.elearning.dto.auth.LoginDTO;
import com.stage.elearning.dto.auth.RegisterDTO;
import com.stage.elearning.dto.user.UserEntityDTOMapper;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.ConfirmationToken;
import com.stage.elearning.model.token.RefreshToken;
import com.stage.elearning.model.token.Token;
import com.stage.elearning.model.token.TokenType;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.ConfirmationTokenRepository;
import com.stage.elearning.repository.RefreshTokenRepository;
import com.stage.elearning.repository.TokenRepository;
import com.stage.elearning.repository.UserEntityRepository;
import com.stage.elearning.security.jwt.JWTService;
import com.stage.elearning.service.auth.AuthService;
import com.stage.elearning.service.auth.AuthServiceImpl;
import com.stage.elearning.service.email.EmailSenderService;
import com.stage.elearning.service.role.RoleService;
import com.stage.elearning.service.token.ConfirmationTokenImpl;
import com.stage.elearning.service.token.RefreshTokenServiceImpl;
import com.stage.elearning.service.token.TokenServiceImpl;
import com.stage.elearning.service.user.UserEntityServiceImpl;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.User;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.catalina.users.MemoryUserDatabase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthService authService;

    @Test
    void testRegister3() {
        AuthService authService = mock(AuthService.class);
        when(authService.register(Mockito.<RegisterDTO>any())).thenReturn(null);
        AuthController authController = new AuthController(authService);
        assertNull(authController
                .register(new RegisterDTO("Dr Jane Doe", "jane.doe@example.org", "42 Main St", "6625550144", "iloveyou")));
        verify(authService).register(Mockito.<RegisterDTO>any());
    }


    @Test
    void testConfirmToken4() {
        AuthService authService = mock(AuthService.class);
        when(authService.confirmToken(Mockito.<String>any())).thenReturn(null);
        assertNull((new AuthController(authService)).confirmToken("ABC123"));
        verify(authService).confirmToken(Mockito.<String>any());
    }


    @Test
    void testRenewAccessToken4() {
        AuthService authService = mock(AuthService.class);
        when(authService.renewAccessToken(Mockito.<String>any(), Mockito.<String>any())).thenReturn(null);
        assertNull((new AuthController(authService)).renewAccessToken("ABC123", "ABC123"));
        verify(authService).renewAccessToken(Mockito.<String>any(), Mockito.<String>any());
    }

    @Test
    void testLogin() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LoginDTO()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testLogin2() throws Exception {
        User user = mock(User.class);
        when(user.getName()).thenReturn("Name");
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/auth/login");
        postResult.principal(new UserDatabaseRealm.UserDatabasePrincipal(user, new MemoryUserDatabase()));
        MockHttpServletRequestBuilder contentTypeResult = postResult.contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LoginDTO()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

