package it.andrea.start.models.audit;

import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "audit_trace",
        indexes = {
                @Index(name = "IDX_AUDIT_USER_NAME", columnList = "username"),
                @Index(name = "IDX_AUDIT_ACTIVITY", columnList = "activity"),
                @Index(name = "IDX_AUDIT_TYPE", columnList = "auditType"),
                @Index(name = "IDX_AUDIT_DATE_EVENT", columnList = "dateEvent"),
                @Index(name = "IDX_AUDIT_RESOURCE_ID", columnList = "resourceId"),
        }
)
public class AuditTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditActivity activity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditTypeOperation auditType;

    @Column(nullable = false)
    private LocalDateTime dateEvent;

    @Column()
    private String username;

    @Column()
    private String className;

    // @Column()
    // private Long userId;

    @Column()
    private String methodName;

    @Column()
    private String controllerMethod;

    @Column()
    private String resourceId;

    @Column()
    private String httpMethod;

    @Column()
    private String requestUri;

    @Column()
    private String clientIpAddress;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column()
    private String userAgent;

    @Column()
    private Integer httpStatus;

    @Column(nullable = false)
    private Boolean success;

    @Column()
    private Long durationMs;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String exceptionTrace;

    public AuditTrace() {
        this.dateEvent = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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
