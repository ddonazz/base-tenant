package it.andrea.start.error.exception.user;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class UserException extends Exception {

    @Serial
    private static final long serialVersionUID = 7266304103349392966L;

    private final transient Object[] messageArguments;

    protected UserException(String message, Throwable cause, Object... args) {
        super(message, cause);
        this.messageArguments = args;
    }

    protected UserException(String message, Object... args) {
        super(message);
        this.messageArguments = args;
    }

}
