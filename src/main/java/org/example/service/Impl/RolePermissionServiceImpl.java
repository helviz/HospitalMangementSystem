package org.example.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.RolePermissionDAO;
import org.example.enums.Role;
import org.example.model.entity.RolePermission;
import org.example.service.Impl.BaseService;
import org.example.service.RolePermissionService;


import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RolePermissionServiceImpl extends BaseService implements RolePermissionService {

    @Inject
    private RolePermissionDAO rolePermissionDAO;

    @Override
    public boolean createRolePermission(RolePermission rolePermission) {
        try {
            // Validate input
            validateRolePermission(rolePermission);

            // Check if role already exists
            if (roleExists(rolePermission.getRoleName())) {
                System.err.println("Role already exists: " + rolePermission.getRoleName());
                return false;
            }

            return rolePermissionDAO.saveRolePermission(rolePermission);
        } catch (Exception e) {
            System.err.println("Error creating role permission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveRolePermission(RolePermission rolePermission) {
        try {
            validateRolePermission(rolePermission);

            // For updates, check if ID exists and it's the same role or role doesn't exist elsewhere
            if (rolePermission.getId() != null) {
                Optional<RolePermission> existing = rolePermissionDAO.findByRoleName(rolePermission.getRoleName());
                if (existing.isPresent() && !existing.get().getId().equals(rolePermission.getId())) {
                    System.err.println("Role already exists with different ID: " + rolePermission.getRoleName());
                    return false;
                }
            } else {
                // For new entries, check if role already exists
                if (roleExists(rolePermission.getRoleName())) {
                    System.err.println("Role already exists: " + rolePermission.getRoleName());
                    return false;
                }
            }

            return rolePermissionDAO.saveRolePermission(rolePermission);
        } catch (Exception e) {
            System.err.println("Error saving role permission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<RolePermission> getRolePermissionById(Long id) {
        try {
            isIdValid(id);
            return rolePermissionDAO.getById(id);
        } catch (Exception e) {
            System.err.println("Error getting role permission by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<RolePermission> findByRoleName(Role role) {
        try {
            if (role == null) {
                throw new IllegalArgumentException("Role cannot be null");
            }
            return rolePermissionDAO.findByRoleName(role);
        } catch (Exception e) {
            System.err.println("Error finding role permission by role name: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<RolePermission> getRolePermissionByName(String roleName) {
        try {
            isNonNullableNameValid(roleName);
            return rolePermissionDAO.findByRoleNameString(roleName);
        } catch (Exception e) {
            System.err.println("Error getting role permission by name: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<RolePermission> getAllRolePermissions() {
        try {
            return rolePermissionDAO.getAllRolePermissions();
        } catch (Exception e) {
            System.err.println("Error getting all role permissions: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean updateRolePermission(RolePermission rolePermission) {
        try {
            if (rolePermission.getId() == null) {
                throw new IllegalArgumentException("ID cannot be null for update operation");
            }

            isIdValid(rolePermission.getId());
            validateRolePermission(rolePermission);

            // Check if the role permission exists
            Optional<RolePermission> existing = rolePermissionDAO.getById(rolePermission.getId());
            if (existing.isEmpty()) {
                System.err.println("Role permission not found for update: " + rolePermission.getId());
                return false;
            }

            // Check if changing to a role that already exists elsewhere
            Optional<RolePermission> existingByRole = rolePermissionDAO.findByRoleName(rolePermission.getRoleName());
            if (existingByRole.isPresent() && !existingByRole.get().getId().equals(rolePermission.getId())) {
                System.err.println("Cannot update: Role already exists with different ID: " + rolePermission.getRoleName());
                return false;
            }

            return rolePermissionDAO.saveRolePermission(rolePermission);
        } catch (Exception e) {
            System.err.println("Error updating role permission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteRolePermission(Long id) {
        try {
            isIdValid(id);
            rolePermissionDAO.deleteRolePermissionById(id);
        } catch (Exception e) {
            System.err.println("Error deleting role permission: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean roleExists(Role role) {
        try {
            if (role == null) {
                return false;
            }
            return rolePermissionDAO.roleExists(role);
        } catch (Exception e) {
            System.err.println("Error checking if role exists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Private method to validate RolePermission input
     */
    private void validateRolePermission(RolePermission rolePermission) {
        if (rolePermission == null) {
            throw new IllegalArgumentException("RolePermission cannot be null");
        }

        if (rolePermission.getRoleName() == null) {
            throw new IllegalArgumentException("Role name cannot be null");
        }

        if (rolePermission.getPermissions() == null || rolePermission.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("Permissions cannot be null or empty");
        }
    }
}