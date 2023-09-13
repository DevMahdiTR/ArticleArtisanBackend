package com.stage.elearning.dto.user;

import com.stage.elearning.model.user.UserEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserEntityDTOMapper implements Function<UserEntity , UserEntityDTO> {
    @Override
    public UserEntityDTO apply(UserEntity userEntity) {
        return new UserEntityDTO(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getEmail(),
                userEntity.getAddress(),
                userEntity.getPhoneNumber(),
                userEntity.getCreatingDate(),
                userEntity.isEnabled(),
                userEntity.getRole()
        );
    }
}
