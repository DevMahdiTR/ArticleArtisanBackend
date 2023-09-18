package com.stage.elearning.service.role;

import com.stage.elearning.model.role.Role;

public interface RoleService {

    public Role fetchRoleByName(final String roleName);
}
