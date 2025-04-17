package it.andrea.start.controller.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 420009367930874140L;

    private String message;
    private final String code;
    private final List<String> details;

    public ErrorResponse(String message, String code, List<String> details) {
        super();
        this.message = message;
        this.code = code;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
