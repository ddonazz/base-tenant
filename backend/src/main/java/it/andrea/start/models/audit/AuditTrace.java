package it.andrea.start.models.audit;

import java.time.LocalDateTime;

import it.andrea.start.models.support.AuditActivity;
import it.andrea.start.models.support.AuditTypeOperation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(
	name = "audit_trace", 
	indexes = { 
		@Index(name = "IDX_AUDIT_USER_ID", columnList = "userId"), 
		@Index(name = "IDX_AUDIT_USER_NAME", columnList = "userName"), 
		@Index(name = "IDX_AUDIT_ACTIVITY", columnList = "activity"), 
		@Index(name = "IDX_AUDIT_TYPE", columnList = "auditType") 
		}
	)
public class AuditTrace {

    public AuditTrace() {
	this.dateEvent = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    @Enumerated(EnumType.STRING)
    private AuditActivity activity;

    @Column()
    private Long userId;

    @Column()
    private String userName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditTypeOperation auditType;

    @Column()
    private LocalDateTime dateEvent;

    @Column()
    private String controllerMethod;

    @Column()
    private String entityName;

    @Column()
    private String entityKeyValue;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String entityOldValue;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String entityNewValue;

    @Column()
    private String method;

    @Column()
    private String url;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String httpContextRequest;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String httpContextResponse;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String exceptionTrace;

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

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
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

    public String getControllerMethod() {
	return controllerMethod;
    }

    public void setControllerMethod(String controllerMethod) {
	this.controllerMethod = controllerMethod;
    }

    public String getEntityName() {
	return entityName;
    }

    public void setEntityName(String entityName) {
	this.entityName = entityName;
    }

    public String getEntityKeyValue() {
	return entityKeyValue;
    }

    public void setEntityKeyValue(String entityKeyValue) {
	this.entityKeyValue = entityKeyValue;
    }

    public String getEntityOldValue() {
	return entityOldValue;
    }

    public void setEntityOldValue(String entityOldValue) {
	this.entityOldValue = entityOldValue;
    }

    public String getEntityNewValue() {
	return entityNewValue;
    }

    public void setEntityNewValue(String entityNewValue) {
	this.entityNewValue = entityNewValue;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getHttpContextRequest() {
	return httpContextRequest;
    }

    public void setHttpContextRequest(String httpContextRequest) {
	this.httpContextRequest = httpContextRequest;
    }

    public String getHttpContextResponse() {
	return httpContextResponse;
    }

    public void setHttpContextResponse(String httpContextResponse) {
	this.httpContextResponse = httpContextResponse;
    }

    public String getExceptionTrace() {
	return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
	this.exceptionTrace = exceptionTrace;
    }

}
