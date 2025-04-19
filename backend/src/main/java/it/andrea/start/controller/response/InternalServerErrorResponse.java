package it.andrea.start.controller.response;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.lang.NonNull;

public record InternalServerErrorResponse(String exceptionMessage, String message) implements Serializable {

    @Serial
    private static final long serialVersionUID = 798539446484745762L;

    @NonNull
    @Override
    public String toString() {
        return "InternalServerErrorResponse [exceptionMessage=" + exceptionMessage + ", message=" + message + "]";
    }

}
