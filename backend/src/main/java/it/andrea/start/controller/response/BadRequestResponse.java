package it.andrea.start.controller.response;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.lang.NonNull;

public record BadRequestResponse(String entity, String message) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7633437953733755422L;

    @NonNull
    @Override
    public String toString() {
        return "BadRequestResponse [entity=" + entity + ", message=" + message + "]";
    }

}
