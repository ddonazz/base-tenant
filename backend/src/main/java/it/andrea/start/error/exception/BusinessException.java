package it.andrea.start.error.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

    @Serial
    private static final long serialVersionUID = 3278937856043871034L;

    private final String entity;
    private final transient Object[] messageArguments;

    public BusinessException(String entity, ErrorCode errorCode, Object... args) {
        super(errorCode.getCode());
        this.entity = entity;
        this.messageArguments = args;
    }

    public BusinessException(String entity, ErrorCode errorCode) {
        super(errorCode.getCode());
        this.entity = entity;
        this.messageArguments = null;
    }
}
