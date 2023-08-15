package com.stage.elearning.service.role;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role fetchRoleByName(String roleName) {
        return roleRepository.fetchRoleByName(roleName).orElseThrow(
                ()-> new ResourceNotFoundException("The role with name : %s could not be found.")
        );
    }
}
