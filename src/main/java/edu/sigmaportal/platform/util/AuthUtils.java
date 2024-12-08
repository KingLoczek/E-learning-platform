package edu.sigmaportal.platform.util;

import edu.sigmaportal.platform.security.UserWithId;
import org.springframework.security.core.Authentication;

public class AuthUtils {

    private AuthUtils() {}

    public static String getUserId(Authentication auth) {
        if (auth.getPrincipal() instanceof UserWithId user) {
            return user.getId();
        } else {
            throw new IllegalStateException("Could not obtain user id from principal");
        }
    }
}
