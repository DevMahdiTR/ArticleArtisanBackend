package com.stage.elearning.service.token;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.RefreshToken;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.RefreshTokenRepository;

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

@ContextConfiguration(classes = {RefreshTokenServiceImpl.class})
@ExtendWith(SpringExtension.class)
class RefreshTokenServiceImplTest {
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Test
    void testGenerateRefreshToken() {
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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);
        when(refreshTokenRepository.save(Mockito.<RefreshToken>any())).thenReturn(refreshToken);

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
        refreshTokenServiceImpl.generateRefreshToken(userEntity2);
        verify(refreshTokenRepository).save(Mockito.<RefreshToken>any());
    }

    @Test
    void testGenerateRefreshToken2() {
        when(refreshTokenRepository.save(Mockito.<RefreshToken>any()))
                .thenThrow(new IllegalStateException("u1vItdC7wzRxwPR29XzNtfDG93PaWsRMIzfezefezYGhjgHJGhjgJHGjhjhgLK9"));

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
        assertThrows(IllegalStateException.class, () -> refreshTokenServiceImpl.generateRefreshToken(userEntity));
        verify(refreshTokenRepository).save(Mockito.<RefreshToken>any());
    }


    @Test
    void testFetchAllRefreshTokenByUserId() {
        ArrayList<RefreshToken> refreshTokenList = new ArrayList<>();
        when(refreshTokenRepository.fetchAllRefreshTokenByUserId(Mockito.<UUID>any())).thenReturn(refreshTokenList);
        List<RefreshToken> actualFetchAllRefreshTokenByUserIdResult = refreshTokenServiceImpl
                .fetchAllRefreshTokenByUserId(UUID.randomUUID());
        assertSame(refreshTokenList, actualFetchAllRefreshTokenByUserIdResult);
        assertTrue(actualFetchAllRefreshTokenByUserIdResult.isEmpty());
        verify(refreshTokenRepository).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
    }

    @Test
    void testFetchAllRefreshTokenByUserId2() {
        when(refreshTokenRepository.fetchAllRefreshTokenByUserId(Mockito.<UUID>any()))
                .thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class,
                () -> refreshTokenServiceImpl.fetchAllRefreshTokenByUserId(UUID.randomUUID()));
        verify(refreshTokenRepository).fetchAllRefreshTokenByUserId(Mockito.<UUID>any());
    }


    @Test
    void testValidateRefreshToken() {
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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> refreshTokenServiceImpl.validateRefreshToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
    }

    @Test
    void testValidateRefreshToken2() {
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
        RefreshToken refreshToken = mock(RefreshToken.class);
        when(refreshToken.isExpired()).thenReturn(false);
        when(refreshToken.isRevoked()).thenReturn(true);
        doNothing().when(refreshToken).setExpired(anyBoolean());
        doNothing().when(refreshToken).setExpiresAt(Mockito.<Date>any());
        doNothing().when(refreshToken).setId(anyLong());
        doNothing().when(refreshToken).setIssuedAt(Mockito.<Date>any());
        doNothing().when(refreshToken).setRefreshToken(Mockito.<String>any());
        doNothing().when(refreshToken).setRevoked(anyBoolean());
        doNothing().when(refreshToken).setUserEntity(Mockito.<UserEntity>any());
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> refreshTokenServiceImpl.validateRefreshToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
        verify(refreshToken).isExpired();
        verify(refreshToken).isRevoked();
        verify(refreshToken).setExpired(anyBoolean());
        verify(refreshToken).setExpiresAt(Mockito.<Date>any());
        verify(refreshToken).setId(anyLong());
        verify(refreshToken).setIssuedAt(Mockito.<Date>any());
        verify(refreshToken).setRefreshToken(Mockito.<String>any());
        verify(refreshToken).setRevoked(anyBoolean());
        verify(refreshToken).setUserEntity(Mockito.<UserEntity>any());
    }


    @Test
    void testValidateRefreshToken3() {
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> refreshTokenServiceImpl.validateRefreshToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
    }

    @Test
    void testSaveAll() {
        when(refreshTokenRepository.saveAll(Mockito.<Iterable<RefreshToken>>any())).thenReturn(new ArrayList<>());
        refreshTokenServiceImpl.saveAll(new ArrayList<>());
        verify(refreshTokenRepository).saveAll(Mockito.<Iterable<RefreshToken>>any());
    }

    /**
     * Method under test: {@link RefreshTokenServiceImpl#saveAll(List)}
     */
    @Test
    void testSaveAll2() {
        when(refreshTokenRepository.saveAll(Mockito.<Iterable<RefreshToken>>any())).thenReturn(new ArrayList<>());

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);

        ArrayList<RefreshToken> refreshTokenList = new ArrayList<>();
        refreshTokenList.add(refreshToken);
        refreshTokenServiceImpl.saveAll(refreshTokenList);
        verify(refreshTokenRepository).saveAll(Mockito.<Iterable<RefreshToken>>any());
    }

    @Test
    void testSaveAll3() {
        when(refreshTokenRepository.saveAll(Mockito.<Iterable<RefreshToken>>any())).thenReturn(new ArrayList<>());

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);

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

        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setExpired(false);
        refreshToken2.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken2.setId(2L);
        refreshToken2.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken2.setRefreshToken("Refresh Token");
        refreshToken2.setRevoked(false);
        refreshToken2.setUserEntity(userEntity2);

        ArrayList<RefreshToken> refreshTokenList = new ArrayList<>();
        refreshTokenList.add(refreshToken2);
        refreshTokenList.add(refreshToken);
        refreshTokenServiceImpl.saveAll(refreshTokenList);
        verify(refreshTokenRepository).saveAll(Mockito.<Iterable<RefreshToken>>any());
    }


    @Test
    void testSaveAll4() {
        when(refreshTokenRepository.saveAll(Mockito.<Iterable<RefreshToken>>any()))
                .thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> refreshTokenServiceImpl.saveAll(new ArrayList<>()));
        verify(refreshTokenRepository).saveAll(Mockito.<Iterable<RefreshToken>>any());
    }


    @Test
    void testFetchRefreshTokenByToken() {
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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpired(true);
        refreshToken.setExpiresAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setId(1L);
        refreshToken.setIssuedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        refreshToken.setRefreshToken("ABC123");
        refreshToken.setRevoked(true);
        refreshToken.setUserEntity(userEntity);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(refreshToken, refreshTokenServiceImpl.fetchRefreshTokenByToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
    }


    @Test
    void testFetchRefreshTokenByToken2() {
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> refreshTokenServiceImpl.fetchRefreshTokenByToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
    }

    @Test
    void testFetchRefreshTokenByToken3() {
        when(refreshTokenRepository.fetchByToken(Mockito.<String>any()))
                .thenThrow(new IllegalStateException("This refresh Token could not be found in our system."));
        assertThrows(IllegalStateException.class, () -> refreshTokenServiceImpl.fetchRefreshTokenByToken("ABC123"));
        verify(refreshTokenRepository).fetchByToken(Mockito.<String>any());
    }
}

