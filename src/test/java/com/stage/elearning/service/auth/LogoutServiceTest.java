package com.stage.elearning.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.RefreshToken;
import com.stage.elearning.model.token.Token;
import com.stage.elearning.model.token.TokenType;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.RefreshTokenRepository;
import com.stage.elearning.repository.TokenRepository;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LogoutService.class})
@ExtendWith(SpringExtension.class)
class LogoutServiceTest {
    @Autowired
    private LogoutService logoutService;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @Test
    void testLogout() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        logoutService.logout(request, response, new TestingAuthenticationToken("Principal", "Credentials"));
        assertFalse(request.isAsyncStarted());
        assertTrue(request.isActive());
        assertTrue(request.getSession() instanceof MockHttpSession);
        assertEquals("", request.getServletPath());
        assertEquals(80, request.getServerPort());
        assertEquals("localhost", request.getServerName());
        assertEquals("http", request.getScheme());
        assertEquals("", request.getRequestURI());
        assertEquals(80, request.getRemotePort());
        assertEquals("localhost", request.getRemoteHost());
        assertEquals("HTTP/1.1", request.getProtocol());
        assertEquals("", request.getMethod());
        assertEquals(80, request.getLocalPort());
        assertEquals("localhost", request.getLocalName());
        assertTrue(request.getInputStream() instanceof DelegatingServletInputStream);
        assertTrue(request.getHttpServletMapping() instanceof MockHttpServletMapping);
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
        assertEquals("", request.getContextPath());
        assertEquals(-1L, request.getContentLengthLong());
    }


    @Test
    void testLogout3() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");
        Response response = new Response();
        logoutService.logout(request, response, new TestingAuthenticationToken("Principal", "Credentials"));
        verify(request).getHeader(Mockito.<String>any());
    }


    @Test
    void testLogout5() {
        when(tokenRepository.findByToken(Mockito.<String>any())).thenReturn(Optional.empty());
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        Response response = new Response();
        logoutService.logout(request, response, new TestingAuthenticationToken("Principal", "Credentials"));
        verify(tokenRepository).findByToken(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    @Test
    void testLogout6() {
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
        Optional<Token> ofResult = Optional.of(token);

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
        when(tokenRepository.save(Mockito.<Token>any())).thenReturn(token2);
        when(tokenRepository.findByToken(Mockito.<String>any())).thenReturn(ofResult);

        Role role4 = new Role();
        role4.setId(1L);
        role4.setName("Name");

        UserEntity userEntity4 = new UserEntity();
        userEntity4.setAddress("42 Main St");
        userEntity4
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity4.setEmail("jane.doe@example.org");
        userEntity4.setEnabled(true);
        userEntity4.setFullName("Dr Jane Doe");
        userEntity4.setId(UUID.randomUUID());
        userEntity4.setPassword("iloveyou");
        userEntity4.setPhoneNumber("6625550144");
        userEntity4.setRole(role4);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity4);

        ArrayList<RefreshToken> refreshTokenList = new ArrayList<>();
        refreshTokenList.add(refreshToken);

        Role role5 = new Role();
        role5.setId(1L);
        role5.setName("Name");

        UserEntity userEntity5 = new UserEntity();
        userEntity5.setAddress("42 Main St");
        userEntity5
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity5.setEmail("jane.doe@example.org");
        userEntity5.setEnabled(true);
        userEntity5.setFullName("Dr Jane Doe");
        userEntity5.setId(UUID.randomUUID());
        userEntity5.setPassword("iloveyou");
        userEntity5.setPhoneNumber("6625550144");
        userEntity5.setRole(role5);

        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setExpired(true);
        refreshToken2.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken2.setId(1L);
        refreshToken2.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken2.setRefreshToken("ABC123");
        refreshToken2.setRevoked(true);
        refreshToken2.setUserEntity(userEntity5);
        when(refreshTokenRepository.save(Mockito.<RefreshToken>any())).thenReturn(refreshToken2);
        when(refreshTokenRepository.fetchAllRefreshTokenByUserId(Mockito.<UUID>any())).thenReturn(refreshTokenList);
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");
        Response response = new Response();
        logoutService.logout(request, response, new TestingAuthenticationToken("Principal", "Credentials"));
        verify(tokenRepository).save(Mockito.<Token>any());
        verify(tokenRepository).findByToken(Mockito.<String>any());
        verify(token).getUserEntity();
        verify(token, atLeast(1)).setExpired(anyBoolean());
        verify(token).setId(anyLong());
        verify(token, atLeast(1)).setRevoked(anyBoolean());
        verify(token).setToken(Mockito.<String>any());
        verify(token).setTokenType(Mockito.<TokenType>any());
        verify(token).setUserEntity(Mockito.<UserEntity>any());
        verify(refreshTokenRepository).save(Mockito.<RefreshToken>any());
        verify(refreshTokenRepository).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}

