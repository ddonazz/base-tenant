package it.andrea.start.exception.user;

public class UserAlreadyExistsException extends UserException {

    private static final long serialVersionUID = 70786780815501035L;

    public UserAlreadyExistsException(String userId) {
	super(userId, "User " + userId + " already exists");
    }

}
