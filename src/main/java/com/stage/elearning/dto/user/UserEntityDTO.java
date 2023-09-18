package com.stage.elearning.dto.user;


import com.stage.elearning.model.role.Role;

import java.util.Date;
import java.util.UUID;


public record UserEntityDTO (
            UUID id,
            String fullName,
            String email,
            String address,
            String phoneNumber,
            Date creationDate,
            boolean isEnabled,
            Role role
        )
{


}
