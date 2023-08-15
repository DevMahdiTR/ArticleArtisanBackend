package com.stage.elearning.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stage.elearning.dto.user.UserEntityDTO;
import com.stage.elearning.dto.user.UserEntityDTOMapper;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.repository.UserEntityRepository;
import com.stage.elearning.service.user.UserEntityServiceImpl;
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
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

class UserEntityControllerTest {

    @Test
    void testFetchUserById() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");

        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("42 Main St");
        Date creatingDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userEntity.setCreatingDate(creatingDate);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setEnabled(true);
        userEntity.setFullName("Dr Jane Doe");
        UUID id = UUID.randomUUID();
        userEntity.setId(id);
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(role);
        UserEntityRepository userEntityRepository = mock(UserEntityRepository.class);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(Optional.of(userEntity));
        UserEntityController userEntityController = new UserEntityController(
                new UserEntityServiceImpl(userEntityRepository, new UserEntityDTOMapper()));
        ResponseEntity<CustomResponseEntity<UserEntityDTO>> actualFetchUserByIdResult = userEntityController
                .fetchUserById(UUID.randomUUID());
        assertTrue(actualFetchUserByIdResult.hasBody());
        assertTrue(actualFetchUserByIdResult.getHeaders().isEmpty());
        assertEquals(200, actualFetchUserByIdResult.getStatusCodeValue());
        CustomResponseEntity<UserEntityDTO> body = actualFetchUserByIdResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        UserEntityDTO data = body.getData();
        assertEquals("42 Main St", data.address());
        assertSame(role, data.role());
        assertEquals("6625550144", data.phoneNumber());
        assertSame(creatingDate, data.creationDate());
        assertEquals("Dr Jane Doe", data.fullName());
        assertTrue(data.isEnabled());
        assertSame(id, data.id());
        assertEquals("jane.doe@example.org", data.email());
        verify(userEntityRepository).fetchUserWithId(Mockito.<UUID>any());
    }


    @Test
    void testFetchAllUsers() {
        UserEntityRepository userEntityRepository = mock(UserEntityRepository.class);
        when(userEntityRepository.fetchAllUsers(Mockito.<Pageable>any())).thenReturn(new ArrayList<>());
        when(userEntityRepository.getTotalUserEntityCount()).thenReturn(3L);
        ResponseEntity<CustomResponseList<UserEntityDTO>> actualFetchAllUsersResult = (new UserEntityController(
                new UserEntityServiceImpl(userEntityRepository, new UserEntityDTOMapper()))).fetchAllUsers(1L);
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
    void testSearchUsers() {
        UserEntityRepository userEntityRepository = mock(UserEntityRepository.class);
        when(userEntityRepository.searchUsers(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any())).thenReturn(new ArrayList<>());
        ResponseEntity<CustomResponseEntity<List<UserEntityDTO>>> actualSearchUsersResult = (new UserEntityController(
                new UserEntityServiceImpl(userEntityRepository, new UserEntityDTOMapper()))).searchUsers("Dr Jane Doe",
                "Adress", "jane.doe@example.org", "6625550144");
        assertTrue(actualSearchUsersResult.hasBody());
        assertTrue(actualSearchUsersResult.getHeaders().isEmpty());
        assertEquals(200, actualSearchUsersResult.getStatusCodeValue());
        CustomResponseEntity<List<UserEntityDTO>> body = actualSearchUsersResult.getBody();
        assertEquals(HttpStatus.OK, body.getStatusString());
        assertEquals(200L, body.getStatus());
        assertTrue(body.getData().isEmpty());
        verify(userEntityRepository).searchUsers(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any(),
                Mockito.<String>any());
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
        UserEntityRepository userEntityRepository = mock(UserEntityRepository.class);
        when(userEntityRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity2);
        when(userEntityRepository.fetchUserWithId(Mockito.<UUID>any())).thenReturn(ofResult);
        UserEntityController userEntityController = new UserEntityController(
                new UserEntityServiceImpl(userEntityRepository, new UserEntityDTOMapper()));
        ResponseEntity<CustomResponseEntity<String>> actualEnableOrDisableUserResult = userEntityController
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
    void testFetchCurrentUser2() {
        UserEntityServiceImpl userEntityService = mock(UserEntityServiceImpl.class);
        when(userEntityService.fetchCurrentUser(Mockito.<UserDetails>any())).thenReturn(null);
        UserEntityController userEntityController = new UserEntityController(userEntityService);
        assertNull(userEntityController.fetchCurrentUser(new UserEntity()));
        verify(userEntityService).fetchCurrentUser(Mockito.<UserDetails>any());
    }
}

