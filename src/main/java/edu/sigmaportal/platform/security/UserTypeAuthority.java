package edu.sigmaportal.platform.security;

import edu.sigmaportal.platform.model.UserTypeModel;
import org.springframework.security.core.GrantedAuthority;

public class UserTypeAuthority implements GrantedAuthority {

    private final String role;
    private final long typeId;

    public UserTypeAuthority(UserTypeModel type) {
        this.role = "user_" + type.name();
        this.typeId = type.id();
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof UserTypeAuthority auth) {
            return typeId == auth.typeId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return role;
    }
}
