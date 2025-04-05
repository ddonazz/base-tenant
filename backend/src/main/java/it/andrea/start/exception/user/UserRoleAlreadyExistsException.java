package it.andrea.start.exception.user;

public class UserRoleAlreadyExistsException extends UserException {
    private static final long serialVersionUID = 9047965170016749306L;

    public UserRoleAlreadyExistsException(String userRole) {
	super(userRole, "User role " + userRole + " already exists");
    }

}
