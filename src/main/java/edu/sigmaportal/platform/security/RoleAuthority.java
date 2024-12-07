package edu.sigmaportal.platform.security;

import edu.sigmaportal.platform.model.PermissionModel;
import org.springframework.security.core.GrantedAuthority;

public class RoleAuthority implements GrantedAuthority {

    private final String role;

    public RoleAuthority(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof RoleAuthority auth) {
            return role.equals(auth.role);
        }

        if (obj instanceof PermissionModel perm) {
            return perm.name().equals(role);
        }

        return false;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
