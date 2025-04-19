package it.andrea.start.utils;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import it.andrea.start.constants.RoleType;

public class HelperAuthorization {

    private HelperAuthorization() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean hasRole(Collection<? extends GrantedAuthority> roles, RoleType role) {
        return roles
                .stream()
                .anyMatch(userRole -> userRole.getAuthority().equals(role.name()));
    }

}
