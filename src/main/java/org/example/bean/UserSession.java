package org.example.session;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.example.enums.Permissions;
import org.example.model.entity.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Named("userSession")
@SessionScoped
public class UserSession implements Serializable {

    private User currentUser;
    private Set<Permissions> userPermissions = new HashSet<>();

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Set<Permissions> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(Set<Permissions> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public boolean hasPermission(Permissions permission) {
        return userPermissions != null && userPermissions.contains(permission);
    }

    public void clear() {
        currentUser = null;
        userPermissions.clear();
    }
}
