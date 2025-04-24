package it.andrea.start.error.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class BusinessException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 3278937856043871034L;

    public BusinessException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

}
