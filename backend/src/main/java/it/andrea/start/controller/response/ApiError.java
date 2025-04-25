package it.andrea.start.controller.response;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.andrea.start.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;
    private List<String> details;

    public ApiError(HttpStatus status, ErrorCode code, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.code = code != null ? code.getCode() : null;
        this.message = message;
        this.path = path;
    }

    public ApiError(HttpStatus status, ErrorCode code, String message, String path, List<String> details) {
        this(status, code, message, path);
        this.details = details;
    }
}
