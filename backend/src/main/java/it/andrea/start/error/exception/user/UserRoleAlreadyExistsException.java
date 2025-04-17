package it.andrea.start.error.exception.user;

import java.io.Serial;

public class UserRoleAlreadyExistsException extends UserException {

    @Serial
    private static final long serialVersionUID = 9047965170016749306L;

    public UserRoleAlreadyExistsException(String userRole) {
        super(userRole, "User role " + userRole + " already exists");
    }

}
