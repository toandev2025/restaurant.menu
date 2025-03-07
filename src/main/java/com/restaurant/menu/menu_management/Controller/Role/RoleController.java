package com.restaurant.menu.menu_management.Controller.Role;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.menu.menu_management.Domain.Role;
import com.restaurant.menu.menu_management.Service.Role.RoleService;
import com.restaurant.menu.menu_management.Util.Error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(this.roleService.fecthAllRole());
    }

    @GetMapping("roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.roleService.fetchRoleById(id));
    }

    @PostMapping("roles/create")
    public ResponseEntity<Role> createNewRole(@RequestBody Role role) {
        return ResponseEntity.ok(this.roleService.handleCreateRole(role));
    }

    @DeleteMapping("roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id must not be greater than 1499");
        }
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.ok("Role Deleted");
    }

    @PutMapping("roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role roleUpdate = this.roleService.handleUpdateRole(role);
        return ResponseEntity.ok().body(roleUpdate);
    }
}