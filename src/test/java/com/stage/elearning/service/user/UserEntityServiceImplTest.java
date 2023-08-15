package com.stage.elearning.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.dto.user.UserEntityDTO;
import com.stage.elearning.dto.user.UserEntityDTOMapper;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.UserEntityRepository;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserEntityServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserEntityServiceImplTest {
    @MockBean
    private UserEntityDTOMapper userEntityDTOMapper;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserEntityServiceImpl userEntityServiceImpl;

    @Test
    void testFetchUserById() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        Date creationDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UserEntityDTO userEntityDTO = new UserEntityDTO(id, "Dr Jane Doe", "jane.doe@example.org", "42 Main St",
                "6625550144", creationDate, true, new Role());

        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any())).thenReturn(userEntityDTO);
        ResponseEntity<CustomResponseEntity<UserEntityDTO>> actualFetchUserByIdResult = userEntityServiceImpl
                .fetchUserById(UUID.randomUUID());
        assertTrue(actualFetchUserByIdResult.hasBody());
        assertTrue(actualFetchUserByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchUserByIdResult.getStatusCodeValue());
        CustomResponseEntity<UserEntityDTO> body = actualFetchUserByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(userEntityDTO, body.getData());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
    }


    @Test
    void testFetchUserById2() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.fetchUserById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
    }


    @Test
    void testFetchUserById3() {
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.fetchUserById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }


    @Test
    void testSearchUser() {
        when(userEntityRepository.searchUsers(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any())).thenReturn(new ArrayList<>());
        ResponseEntity<CustomResponseEntity<List<UserEntityDTO>>> actualSearchUserResult = userEntityServiceImpl
                .searchUser("Dr Jane Doe", "42 Main St", "jane.doe@example.org", "6625550144");
        assertTrue(actualSearchUserResult.hasBody());
        assertTrue(actualSearchUserResult.getHeaders().isEmpty());
        assertEquals(200, actualSearchUserResult.getStatusCodeValue());
        CustomResponseEntity<List<UserEntityDTO>> body = actualSearchUserResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(userEntityRepository).searchUsers(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any());
    }


    @Test
    void testFetchAllUsers() {
        when(userEntityRepository.fetchAllUsers(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(userEntityRepository.getTotalUserEntityCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<UserEntityDTO>> actualFetchAllUsersResult = userEntityServiceImpl
                .fetchAllUsers(1L);
        assertTrue(actualFetchAllUsersResult.hasBody());
        assertTrue(actualFetchAllUsersResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllUsersResult.getStatusCodeValue());
        CustomResponseList<UserEntityDTO> body = actualFetchAllUsersResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertTrue(body.getData().isEmpty());
        assertEquals(0L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(userEntityRepository).fetchAllUsers(Mockito.<Pageable>any());
        verify(userEntityRepository).getTotalUserEntityCount();
    }

    @Test
    void testFetchAllUsers2() {
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

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userEntityRepository.fetchAllUsers(Mockito.<Pageable>any())).thenReturn(userEntityList);
        when(userEntityRepository.getTotalUserEntityCount()).thenReturn(3L);
        UUID id = UUID.randomUUID();
        Date creationDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any())).thenReturn(new UserEntityDTO(id, "Dr Jane Doe",
                "jane.doe@example.org", "42 Main St", "6625550144", creationDate, true, new Role()));
        ResponseEntity<CustomResponseList<UserEntityDTO>> actualFetchAllUsersResult = userEntityServiceImpl
                .fetchAllUsers(1L);
        assertTrue(actualFetchAllUsersResult.hasBody());
        assertTrue(actualFetchAllUsersResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchAllUsersResult.getStatusCodeValue());
        CustomResponseList<UserEntityDTO> body = actualFetchAllUsersResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(1, body.getData().size());
        assertEquals(1L, body.getCurrentLength());
        assertEquals(3L, body.getTotal());
        assertEquals(200L, body.getStatus());
        verify(userEntityRepository).fetchAllUsers(Mockito.<Pageable>any());
        verify(userEntityRepository).getTotalUserEntityCount();
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
    }


    @Test
    void testFetchCurrentUser() {
        assertThrows(IllegalArgumentException.class, () -> userEntityServiceImpl.fetchCurrentUser(new UserEntity()));
        assertThrows(IllegalArgumentException.class, () -> userEntityServiceImpl.fetchCurrentUser(null));
    }

    @Test
    void testFetchCurrentUser2() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any())).thenReturn(ofResult);
        UUID id = UUID.randomUUID();
        Date creationDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UserEntityDTO userEntityDTO = new UserEntityDTO(id, "Dr Jane Doe", "jane.doe@example.org", "42 Main St",
                "6625550144", creationDate, true, new Role());

        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any())).thenReturn(userEntityDTO);
        ResponseEntity<CustomResponseEntity<UserEntityDTO>> actualFetchCurrentUserResult = userEntityServiceImpl
                .fetchCurrentUser(new User("janedoe", "iloveyou", new ArrayList<>()));
        assertTrue(actualFetchCurrentUserResult.hasBody());
        assertTrue(actualFetchCurrentUserResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchCurrentUserResult.getStatusCodeValue());
        CustomResponseEntity<UserEntityDTO> body = actualFetchCurrentUserResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertSame(userEntityDTO, body.getData());
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
    }


    @Test
    void testFetchCurrentUser3() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(userEntityDTOMapper.apply(Mockito.<UserEntity>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.fetchCurrentUser(new User("janedoe", "iloveyou", new ArrayList<>())));
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
        verify(userEntityDTOMapper).apply(Mockito.<UserEntity>any());
    }


    @Test
    void testFetchCurrentUser5() {
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.fetchCurrentUser(new User("janedoe", "iloveyou", new ArrayList<>())));
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
    }

    @Test
    void testEnableOrDisableUser() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setAddress("42 Main St");
        userEntity2.setCreatingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setEnabled(true);
        userEntity2.setFullName("Dr Jane Doe");
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setPassword("iloveyou");
        userEntity2.setPhoneNumber("6625550144");
        userEntity2.setRole(role2);
        when(userEntityRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity2);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        ResponseEntity<CustomResponseEntity<String>> actualEnableOrDisableUserResult = userEntityServiceImpl
                .enableOrDisableUser(UUID.randomUUID(), true);
        assertTrue(actualEnableOrDisableUserResult.hasBody());
        assertTrue(actualEnableOrDisableUserResult.getHeaders().isEmpty());
        assertEquals(200, actualEnableOrDisableUserResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualEnableOrDisableUserResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The user with email : jane.doe@example.org  enabled = true", body.getData());
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testEnableOrDisableUser2() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.save(Mockito.<UserEntity>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.enableOrDisableUser(UUID.randomUUID(), true));
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }


    @Test
    void testEnableOrDisableUser4() {
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.enableOrDisableUser(UUID.randomUUID(), true));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testEnableOrDisableUser5() {
        assertThrows(IllegalArgumentException.class, () -> userEntityServiceImpl.enableOrDisableUser(null, true));
    }

    @Test
    void testEnableOrDisableUser6() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);

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
        when(userEntityRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity2);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        ResponseEntity<CustomResponseEntity<String>> actualEnableOrDisableUserResult = userEntityServiceImpl
                .enableOrDisableUser(UUID.randomUUID(), false);
        assertTrue(actualEnableOrDisableUserResult.hasBody());
        assertTrue(actualEnableOrDisableUserResult.getHeaders().isEmpty());
        assertEquals(200, actualEnableOrDisableUserResult.getStatusCodeValue());
        CustomResponseEntity<String> body = actualEnableOrDisableUserResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertEquals("The user with email : jane.doe@example.org  enabled = false", body.getData());
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testEnableUserById() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);

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
        when(userEntityRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity2);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        userEntityServiceImpl.enableUserById(UUID.randomUUID());
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testEnableUserById2() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.save(Mockito.<UserEntity>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.enableUserById(UUID.randomUUID()));
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testEnableUserById3() {
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.enableUserById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testIsFullNameRegistered() {
        when(userEntityRepository.isFullNameRegistered(Mockito.<String>any())).thenReturn(true);
        assertTrue(userEntityServiceImpl.isFullNameRegistered("Dr Jane Doe"));
        verify(userEntityRepository).isFullNameRegistered(Mockito.<String>any());
    }

    @Test
    void testIsFullNameRegistered2() {
        when(userEntityRepository.isFullNameRegistered(Mockito.<String>any())).thenReturn(false);
        assertFalse(userEntityServiceImpl.isFullNameRegistered("Dr Jane Doe"));
        verify(userEntityRepository).isFullNameRegistered(Mockito.<String>any());
    }

    @Test
    void testIsFullNameRegistered3() {
        when(userEntityRepository.isFullNameRegistered(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.isFullNameRegistered("Dr Jane Doe"));
        verify(userEntityRepository).isFullNameRegistered(Mockito.<String>any());
    }

    @Test
    void testIsEmailRegistered() {
        when(userEntityRepository.isEmailRegistered(Mockito.<String>any())).thenReturn(true);
        assertTrue(userEntityServiceImpl.isEmailRegistered("jane.doe@example.org"));
        verify(userEntityRepository).isEmailRegistered(Mockito.<String>any());
    }

    @Test
    void testIsEmailRegistered2() {
        when(userEntityRepository.isEmailRegistered(Mockito.<String>any())).thenReturn(false);
        assertFalse(userEntityServiceImpl.isEmailRegistered("jane.doe@example.org"));
        verify(userEntityRepository).isEmailRegistered(Mockito.<String>any());
    }

    @Test
    void testIsEmailRegistered3() {
        when(userEntityRepository.isEmailRegistered(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.isEmailRegistered("jane.doe@example.org"));
        verify(userEntityRepository).isEmailRegistered(Mockito.<String>any());
    }

    @Test
    void testIsPhoneNumberRegistered() {
        when(userEntityRepository.isPhoneNumberRegistered(Mockito.<String>any())).thenReturn(true);
        assertTrue(userEntityServiceImpl.isPhoneNumberRegistered("6625550144"));
        verify(userEntityRepository).isPhoneNumberRegistered(Mockito.<String>any());
    }

    @Test
    void testIsPhoneNumberRegistered2() {
        when(userEntityRepository.isPhoneNumberRegistered(Mockito.<String>any())).thenReturn(false);
        assertFalse(userEntityServiceImpl.isPhoneNumberRegistered("6625550144"));
        verify(userEntityRepository).isPhoneNumberRegistered(Mockito.<String>any());
    }

    @Test
    void testIsPhoneNumberRegistered3() {
        when(userEntityRepository.isPhoneNumberRegistered(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.isPhoneNumberRegistered("6625550144"));
        verify(userEntityRepository).isPhoneNumberRegistered(Mockito.<String>any());
    }

    @Test
    void testSaveUser() {
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
        when(userEntityRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity);

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
        assertSame(userEntity, userEntityServiceImpl.saveUser(userEntity2));
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
    }

    @Test
    void testSaveUser2() {
        when(userEntityRepository.save(Mockito.<UserEntity>any()))
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
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.saveUser(userEntity));
        verify(userEntityRepository).save(Mockito.<UserEntity>any());
    }

    @Test
    void testGetUserEntityById() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        assertSame(userEntity, userEntityServiceImpl.getUserEntityById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testGetUserEntityById2() {
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.getUserEntityById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testGetUserEntityById3() {
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userEntityServiceImpl.getUserEntityById(UUID.randomUUID()));
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }

    @Test
    void testGetUserEntityByEmail() {
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
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any())).thenReturn(ofResult);
        assertSame(userEntity, userEntityServiceImpl.getUserEntityByEmail("jane.doe@example.org"));
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
    }


    @Test
    void testGetUserEntityByEmail3() {
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.getUserEntityByEmail("jane.doe@example.org"));
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
    }

    @Test
    void testGetUserEntityByEmail4() {
        assertThrows(IllegalArgumentException.class, () -> userEntityServiceImpl.getUserEntityByEmail(null));
    }

    @Test
    void testGetUserEntityByEmail5() {
        when(userEntityRepository.fetchUserWithEmail(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class,
                () -> userEntityServiceImpl.getUserEntityByEmail("jane.doe@example.org"));
        verify(userEntityRepository).fetchUserWithEmail(Mockito.<String>any());
    }
}

