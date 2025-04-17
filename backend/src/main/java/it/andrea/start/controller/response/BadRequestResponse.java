package it.andrea.start.controller.response;

import java.io.Serial;
import java.io.Serializable;

public record BadRequestResponse(String entity, String exceptionMessage, String message) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7633437953733755422L;

    @Override
    public String toString() {
        return "BadRequestResponse [entity=" + entity + ", exceptionMessage=" + exceptionMessage + ", message=" + message + "]";
    }

}
