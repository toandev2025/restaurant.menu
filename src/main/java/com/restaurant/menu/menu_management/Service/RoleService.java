package com.restaurant.menu.menu_management.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.restaurant.menu.menu_management.Domain.Role;
import com.restaurant.menu.menu_management.Repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> fecthAllRole() {
        return this.roleRepository.findAll();
    }

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public Role handleCreateRole(Role role) {
        return this.roleRepository.save(role);
    }

    public void handleDeleteRole(long id) {
        this.roleRepository.deleteById(id);
    }

    public Role handleUpdateRole(Role role) {
        Role roleUpdate = this.fetchRoleById(role.getId());

        if (roleUpdate != null) {
            roleUpdate.setName(role.getName());

            // update
            roleUpdate = this.roleRepository.save(roleUpdate);
        }

        return roleUpdate;
    }

}
