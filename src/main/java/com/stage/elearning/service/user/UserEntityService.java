package com.stage.elearning.service.user;

import com.stage.elearning.dto.user.UserEntityDTO;
import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserEntityService {
    public ResponseEntity<CustomResponseEntity<UserEntityDTO>> fetchUserById(final UUID userId);
    public ResponseEntity<CustomResponseEntity<List<UserEntityDTO>>> searchUser(String fullName, String address, String email, String phoneNumber);
    public ResponseEntity<CustomResponseList<UserEntityDTO>> fetchAllUsers(final long pageNumber);
    public ResponseEntity<CustomResponseEntity<UserEntityDTO>> fetchCurrentUser(final UserDetails userDetails);
    public ResponseEntity<CustomResponseEntity<String>> enableOrDisableUser(@NotNull final UUID userId , final boolean enabled);
    public void enableUserById(final UUID userId);
    public UserEntity getUserEntityById(final UUID userId);
    public UserEntity getUserEntityByEmail(final String email);
    public boolean isFullNameRegistered(final String fullName);
    public boolean isEmailRegistered(final String email);
    public boolean isPhoneNumberRegistered(final String phoneNumber);
    public UserEntity saveUser(@NotNull final UserEntity userEntity);
}
