package it.andrea.start.error.exception;

import java.io.Serial;

public abstract class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8236888816462673842L;

    private final ErrorCode errorCode;
    private final transient Object[] messageArguments;

    protected ApplicationException(ErrorCode errorCode, String message, Throwable cause, Object... messageArguments) {
        super(message, cause);
        this.errorCode = errorCode;
        this.messageArguments = messageArguments;
    }

    protected ApplicationException(ErrorCode errorCode, String message, Object... messageArguments) {
        super(message);
        this.errorCode = errorCode;
        this.messageArguments = messageArguments;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getMessageArguments() {
        return messageArguments;
    }
}