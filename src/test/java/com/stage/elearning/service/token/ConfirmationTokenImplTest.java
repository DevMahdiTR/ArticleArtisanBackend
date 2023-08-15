package com.stage.elearning.service.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.token.ConfirmationToken;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.ConfirmationTokenRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ConfirmationTokenImpl.class})
@ExtendWith(SpringExtension.class)
class ConfirmationTokenImplTest {
    @Autowired
    private ConfirmationTokenImpl confirmationTokenImpl;

    @MockBean
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    void testSaveConfirmationToken() {
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
        when(confirmationTokenRepository.save(Mockito.<ConfirmationToken>any())).thenReturn(confirmationToken);

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

        ConfirmationToken confirmationToken2 = new ConfirmationToken();
        confirmationToken2.setConfirmedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setExpiresAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setId(1L);
        confirmationToken2.setToken("ABC123");
        confirmationToken2.setUserEntity(userEntity2);
        confirmationTokenImpl.saveConfirmationToken(confirmationToken2);
        verify(confirmationTokenRepository).save(Mockito.<ConfirmationToken>any());
        assertSame(userEntity2, confirmationToken2.getUserEntity());
        assertEquals("00:00", confirmationToken2.getConfirmedAt().toLocalTime().toString());
        assertEquals(1L, confirmationToken2.getId());
        assertEquals("ABC123", confirmationToken2.getToken());
        assertEquals("1970-01-01", confirmationToken2.getCreatedAt().toLocalDate().toString());
        assertEquals("1970-01-01", confirmationToken2.getExpiresAt().toLocalDate().toString());
    }


    @Test
    void testFetchTokenByToken() {
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
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(confirmationToken, confirmationTokenImpl.fetchTokenByToken("ABC123"));
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }

    @Test
    void testFetchTokenByToken2() {
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> confirmationTokenImpl.fetchTokenByToken("ABC123"));
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }


    @Test
    void testFetchTokenByToken3() {
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> confirmationTokenImpl.fetchTokenByToken("ABC123"));
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }

    @Test
    void testSetConfirmedAt() {
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
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);

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

        ConfirmationToken confirmationToken2 = new ConfirmationToken();
        confirmationToken2.setConfirmedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setExpiresAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        confirmationToken2.setId(1L);
        confirmationToken2.setToken("ABC123");
        confirmationToken2.setUserEntity(userEntity2);
        when(confirmationTokenRepository.save(Mockito.<ConfirmationToken>any())).thenReturn(confirmationToken2);
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any())).thenReturn(ofResult);
        confirmationTokenImpl.setConfirmedAt("ABC123");
        verify(confirmationTokenRepository).save(Mockito.<ConfirmationToken>any());
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }

    @Test
    void testSetConfirmedAt2() {
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
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);
        when(confirmationTokenRepository.save(Mockito.<ConfirmationToken>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> confirmationTokenImpl.setConfirmedAt("ABC123"));
        verify(confirmationTokenRepository).save(Mockito.<ConfirmationToken>any());
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }


    @Test
    void testSetConfirmedAt3() {
        when(confirmationTokenRepository.fetchConfirmationTokenByToken(Mockito.<String>any()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> confirmationTokenImpl.setConfirmedAt("ABC123"));
        verify(confirmationTokenRepository).fetchConfirmationTokenByToken(Mockito.<String>any());
    }


    @Test
    void testGenerateConfirmationToken() {
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
        when(confirmationTokenRepository.save(Mockito.<ConfirmationToken>any())).thenReturn(confirmationToken);

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
        confirmationTokenImpl.generateConfirmationToken(userEntity2);
        verify(confirmationTokenRepository).save(Mockito.<ConfirmationToken>any());
    }

    @Test
    void testGenerateConfirmationToken2() {
        when(confirmationTokenRepository.save(Mockito.<ConfirmationToken>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

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
        assertThrows(ResourceNotFoundException.class, () -> confirmationTokenImpl.generateConfirmationToken(userEntity));
        verify(confirmationTokenRepository).save(Mockito.<ConfirmationToken>any());
    }
}

