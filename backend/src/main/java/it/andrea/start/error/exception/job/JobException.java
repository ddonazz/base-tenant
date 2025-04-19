package it.andrea.start.error.exception.job;

import java.io.Serial;

import lombok.Getter;

@Getter
public abstract class JobException extends Exception {

    @Serial
    private static final long serialVersionUID = -319912046240825475L;

    private final transient Object[] messageArguments;

    protected JobException(String message, Throwable cause, Object... args) {
        super(message, cause);
        this.messageArguments = args;
    }

    protected JobException(String message, Object... args) {
        super(message);
        this.messageArguments = args;
    }

}
