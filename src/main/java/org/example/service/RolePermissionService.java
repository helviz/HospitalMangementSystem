package org.example.service;

import org.example.enums.Role;
import org.example.model.entity.RolePermission;

import java.util.List;
import java.util.Optional;

public interface RolePermissionService {


    boolean createRolePermission(RolePermission rolePermission);
    boolean saveRolePermission(RolePermission rolePermission);
    Optional<RolePermission> getRolePermissionById(Long id);
    Optional<RolePermission> findByRoleName(Role role);
    Optional<RolePermission> getRolePermissionByName(String roleName);
    List<RolePermission> getAllRolePermissions();
    boolean updateRolePermission(RolePermission rolePermission);
    void deleteRolePermission(Long id);
    boolean roleExists(Role role);
}