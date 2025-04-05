package it.andrea.start.exception.user;

public class UserNotFoundException extends UserException {
    private static final long serialVersionUID = 6236059369592609596L;

    public UserNotFoundException(String userId) {
	super(userId, "User " + userId + " not found");
    }

}
