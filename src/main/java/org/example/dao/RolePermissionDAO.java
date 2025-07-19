package org.example.dao;

import org.example.dao.BaseDAO;
import org.example.enums.Role;
import org.example.model.entity.RolePermission;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RolePermissionDAO extends BaseDAO<RolePermission> {

    public RolePermissionDAO() {
        super(RolePermission.class);
    }

    /**
     * Find RolePermission by Role enum
     */
    public Optional<RolePermission> findByRoleName(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RolePermission> query = session.createQuery(
                    "FROM RolePermission WHERE roleName = :roleName AND deleted = false",
                    RolePermission.class
            );
            query.setParameter("roleName", role);
            List<RolePermission> result = query.getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception e) {
            System.err.println("Error finding RolePermission by role name: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Find RolePermission by Role name string (for backward compatibility)
     */
    public Optional<RolePermission> findByRoleNameString(String roleName) {
        try {
            Role role = Role.valueOf(roleName.toUpperCase());
            return findByRoleName(role);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid role name: " + roleName);
            return Optional.empty();
        }
    }

    /**
     * Get all non-deleted RolePermissions
     */
    public List<RolePermission> getAllRolePermissions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RolePermission> query = session.createQuery(
                    "FROM RolePermission WHERE deleted = false ORDER BY roleName",
                    RolePermission.class
            );
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all role permissions: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list instead of null
        }
    }

    /**
     * Save or update RolePermission
     */
    public boolean saveRolePermission(RolePermission rolePermission) {
        try {
            return saveToDB(rolePermission);
        } catch (Exception e) {
            System.err.println("Error saving role permission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Soft delete RolePermission by ID
     */
    public void deleteRolePermissionById(Long id) {
        try {
            deleteByID(id); // This should use soft delete from BaseDAO
        } catch (Exception e) {
            System.err.println("Error deleting role permission with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if a role already exists
     */
    public boolean roleExists(Role role) {
        return findByRoleName(role).isPresent();
    }

    /**
     * Get RolePermission by ID with proper error handling
     */
    @Override
    public Optional<RolePermission> getById(Long id) {
        try {
            return super.getById(id);
        } catch (Exception e) {
            System.err.println("Error getting role permission by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}