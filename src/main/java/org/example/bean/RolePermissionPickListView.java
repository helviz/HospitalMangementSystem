package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.annotation.RequiresPermission;
import org.example.enums.Permissions;
import org.example.enums.Role;
import org.example.model.entity.RolePermission;
import org.example.service.RolePermissionService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.security.Permission;
import java.util.*;

@Named
@RequestScoped
public class RolePermissionPickListView implements Serializable {

    private DualListModel<Permissions> permissions;

    @Inject
    private RolePermissionService rolePermissionService;

    private Role selectedRole;

    @PostConstruct
    public void init() {
        List<Permissions> allPermissions = Arrays.asList(Permissions.values());
        permissions = new DualListModel<>(allPermissions, new ArrayList<>());
        List<Permission> source = new ArrayList<>(); // Available permissions
        List<Permission> target = new ArrayList<>(); // Assigned permissions

    }

    // Load existing permissions for a role
    public void loadRolePermissions() {
        if (selectedRole != null) {
            // Load existing permissions for the selected role
            RolePermission existingRolePermission = rolePermissionService.findByRoleName(selectedRole).orElse(null);

            List<Permissions> allPermissions = Arrays.asList(Permissions.values());
            List<Permissions> assignedPermissions = new ArrayList<>();
            List<Permissions> availablePermissions = new ArrayList<>(allPermissions);

            if (existingRolePermission != null && existingRolePermission.getPermissions() != null) {
                assignedPermissions.addAll(existingRolePermission.getPermissions());
                availablePermissions.removeAll(assignedPermissions);
            }

            permissions = new DualListModel<>(availablePermissions, assignedPermissions);
        }
    }

    // Ajax event methods
    @RequiresPermission(Permissions.CREATE_PERMISSION)
    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder("Transferred: <br/>");
        for (Object item : event.getItems()) {
            Permissions perm = (Permissions) item;
            builder.append(perm.name()).append("<br />");
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Permissions Transferred", builder.toString()));
    }

    public void onSelect(SelectEvent<Permissions> event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getObject().name()));
    }

    public void onUnselect(UnselectEvent<Permissions> event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getObject().name()));
    }

    public void onReorder() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }

    // Business method for saving the role
    @RequiresPermission(Permissions.CREATE_PERMISSION)
    public void saveRoleWithPermissions() {
        if (selectedRole == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a role first."));
            return;
        }

        try {
            Optional<RolePermission> existingOpt = rolePermissionService.findByRoleName(selectedRole);
            RolePermission rolePermission;

            if (existingOpt.isPresent()) {
                // ✅ Update the existing managed entity
                rolePermission = existingOpt.get();
                rolePermission.setPermissions(new HashSet<>(permissions.getTarget()));

            } else {
                // ✅ Create new only if doesn't exist
                rolePermission = new RolePermission();
                rolePermission.setRoleName(selectedRole);
                rolePermission.setPermissions(new HashSet<>(permissions.getTarget()));

            }

            boolean saved = rolePermissionService.saveRolePermission(rolePermission);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(saved ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                            saved ? "Success" : "Failure",
                            saved ? "Role permissions saved successfully!" : "Failed to save role permissions."));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occurred while saving: " + e.getMessage()));
        }
    }


    // Reset form
    public void resetForm() {
        selectedRole = null;
        List<Permissions> allPermissions = Arrays.asList(Permissions.values());
        permissions = new DualListModel<>(allPermissions, new ArrayList<>());
    }

    // Utility methods
    public List<Permissions> getAllPermissions() {
        return Arrays.asList(Permissions.values());
    }

    public Role[] getAllRoles() {
        return Role.values();
    }

    // Getters and Setters
    public DualListModel<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(DualListModel<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
        // Auto-load permissions when role is selected
        loadRolePermissions();
    }
}