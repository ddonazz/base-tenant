package it.andrea.start.dto.audit;

import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditTraceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime dateEvent;

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

    private Integer httpStatus;
    private Boolean success;
    private Long durationMs;
    private String exceptionTrace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    public AuditActivity getActivity() {
        return activity;
    }

    public void setActivity(AuditActivity activity) {
        this.activity = activity;
    }

    public AuditTypeOperation getAuditType() {
        return auditType;
    }

    public void setAuditType(AuditTypeOperation auditType) {
        this.auditType = auditType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(String controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getExceptionTrace() {
        return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

}