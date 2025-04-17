package it.andrea.start.error.exception.user;

import java.io.Serial;

public abstract class UserException extends Exception {

    @Serial
    private static final long serialVersionUID = 7266304103349392966L;

    private final String userId;

    protected UserException(String userId, String message) {
        super(message);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

}
