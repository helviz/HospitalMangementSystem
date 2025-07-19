package org.example.model.entity;

import jakarta.persistence.*;
import org.example.enums.Permissions;
import org.example.enums.Role;
import org.example.model.base.SoftDeletable;

import java.util.Set;

@Entity
@Table(name = "role_permission")
public class RolePermission implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    private Role roleName;

    @ElementCollection(targetClass = Permissions.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_permission_id"))
    @Column(name = "permission")
    private Set<Permissions> permissions;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // Constructors
    public RolePermission() {}

    public RolePermission(Role roleName, Set<Permissions> permissions) {
        this.roleName = roleName;
        this.permissions = permissions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRoleName() {
        return roleName;
    }

    public void setRoleName(Role roleName) {
        this.roleName = roleName;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean isDelete() {
        return deleted;
    }

    @Override
    public void setDelete(boolean deleted) {
        this.deleted = deleted;
    }
}