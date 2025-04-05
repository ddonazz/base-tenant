package it.andrea.start.exception.user;

public class UserRoleNotFoundException extends UserException {
    private static final long serialVersionUID = -2104139028316052461L;

    public UserRoleNotFoundException(String userRoleName) {
	super(userRoleName, "User role " + userRoleName + " not found");
    }

}
