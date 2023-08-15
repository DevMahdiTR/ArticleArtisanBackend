package com.stage.elearning.service.token;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.Token;
import com.stage.elearning.model.token.TokenType;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.TokenRepository;
import com.stage.elearning.security.jwt.JWTService;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TokenServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TokenServiceImplTest {
    @MockBean
    private JWTService jWTService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;


    @Test
    void testGetTokenByToken() {
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
        Optional<Token> ofResult = Optional.of(token);
        when(tokenRepository.findByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(token, tokenServiceImpl.getTokenByToken("ABC123"));
        verify(tokenRepository).findByToken(Mockito.<String>any());
    }


    @Test
    void testGetTokenByToken2() {
        when(tokenRepository.findByToken(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.getTokenByToken("ABC123"));
        verify(tokenRepository).findByToken(Mockito.<String>any());
    }

    @Test
    void testGetTokenByToken3() {
        when(tokenRepository.findByToken(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.getTokenByToken("ABC123"));
        verify(tokenRepository).findByToken(Mockito.<String>any());
    }

    @Test
    void testFetchAllValidTokenByUserId() {
        ArrayList<Token> tokenList = new ArrayList<>();
        when(tokenRepository.fetchAllValidTokenByUserId(Mockito.<UUID>any())).thenReturn(tokenList);
        List<Token> actualFetchAllValidTokenByUserIdResult = tokenServiceImpl.fetchAllValidTokenByUserId(UUID.randomUUID());
        assertSame(tokenList, actualFetchAllValidTokenByUserIdResult);
        assertTrue(actualFetchAllValidTokenByUserIdResult.isEmpty());
        verify(tokenRepository).fetchAllValidTokenByUserId(Mockito.<UUID>any());
    }


    @Test
    void testFetchAllValidTokenByUserId2() {
        when(tokenRepository.fetchAllValidTokenByUserId(Mockito.<UUID>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class,
                () -> tokenServiceImpl.fetchAllValidTokenByUserId(UUID.randomUUID()));
        verify(tokenRepository).fetchAllValidTokenByUserId(Mockito.<UUID>any());
    }

    @Test
    void testSave() {
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
        when(tokenRepository.save(Mockito.<Token>any())).thenReturn(token);

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

        Token token2 = new Token();
        token2.setExpired(true);
        token2.setId(1L);
        token2.setRevoked(true);
        token2.setToken("ABC123");
        token2.setTokenType(TokenType.BEARER);
        token2.setUserEntity(userEntity2);
        assertSame(token, tokenServiceImpl.save(token2));
        verify(tokenRepository).save(Mockito.<Token>any());
    }


    @Test
    void testSave2() {
        when(tokenRepository.save(Mockito.<Token>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

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
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.save(token));
        verify(tokenRepository).save(Mockito.<Token>any());
    }


    @Test
    void testSaveAll() {
        ArrayList<Token> tokenList = new ArrayList<>();
        when(tokenRepository.saveAll(Mockito.<Iterable<Token>>any())).thenReturn(tokenList);
        List<Token> actualSaveAllResult = tokenServiceImpl.saveAll(new ArrayList<>());
        assertSame(tokenList, actualSaveAllResult);
        assertTrue(actualSaveAllResult.isEmpty());
        verify(tokenRepository).saveAll(Mockito.<Iterable<Token>>any());
    }

    @Test
    void testSaveAll2() {
        ArrayList<Token> tokenList = new ArrayList<>();
        when(tokenRepository.saveAll(Mockito.<Iterable<Token>>any())).thenReturn(tokenList);

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

        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(token);
        List<Token> actualSaveAllResult = tokenServiceImpl.saveAll(tokens);
        assertSame(tokenList, actualSaveAllResult);
        assertTrue(actualSaveAllResult.isEmpty());
        verify(tokenRepository).saveAll(Mockito.<Iterable<Token>>any());
    }


    @Test
    void testSaveAll3() {
        ArrayList<Token> tokenList = new ArrayList<>();
        when(tokenRepository.saveAll(Mockito.<Iterable<Token>>any())).thenReturn(tokenList);

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

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("com.stage.elearning.model.role.Role");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("17 High St");
        userEntity2
                .setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("john.smith@example.org");
        userEntity2.setEnabled(false);
        userEntity2.setFullName("Mr John Smith");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("Password");
        userEntity2.setPhoneNumber("8605550118");
        userEntity2.setRole(role2);

        Token token2 = new Token();
        token2.setExpired(false);
        token2.setId(2L);
        token2.setRevoked(false);
        token2.setToken("Token");
        token2.setTokenType(TokenType.BEARER);
        token2.setUserEntity(userEntity2);

        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(token2);
        tokens.add(token);
        List<Token> actualSaveAllResult = tokenServiceImpl.saveAll(tokens);
        assertSame(tokenList, actualSaveAllResult);
        assertTrue(actualSaveAllResult.isEmpty());
        verify(tokenRepository).saveAll(Mockito.<Iterable<Token>>any());
    }

    @Test
    void testSaveAll4() {
        when(tokenRepository.saveAll(Mockito.<Iterable<Token>>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> tokenServiceImpl.saveAll(new ArrayList<>()));
        verify(tokenRepository).saveAll(Mockito.<Iterable<Token>>any());
    }
}

