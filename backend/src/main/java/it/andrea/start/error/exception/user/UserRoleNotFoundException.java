package it.andrea.start.error.exception.user;

import java.io.Serial;

public class UserRoleNotFoundException extends UserException {

    @Serial
    private static final long serialVersionUID = -2104139028316052461L;

    public UserRoleNotFoundException(String userRoleName) {
        super(userRoleName, "User role " + userRoleName + " not found");
    }

}
