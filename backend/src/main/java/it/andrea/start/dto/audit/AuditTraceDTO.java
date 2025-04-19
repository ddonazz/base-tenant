package it.andrea.start.dto.audit;

import java.io.Serial;
import java.io.Serializable;

import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditTraceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String dateEvent;

    private AuditActivity activity;
    private AuditTypeOperation auditType;

    private Long userId;
    private String username;

    private String className;
    private String methodName;
    private String controllerMethod;
    private String resourceId;

    private String httpMethod;
    private String requestUri;
    private String clientIpAddress;
    private String userAgent;
    private String requestParams;
    private String requestBody;

    private Boolean success;
    private Long durationMs;

    private String exceptionType;
    private String exceptionMessage;
}