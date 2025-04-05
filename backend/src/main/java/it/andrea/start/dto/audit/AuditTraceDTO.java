package it.andrea.start.dto.audit;

import java.io.Serializable;
import java.time.LocalDateTime;

import it.andrea.start.models.support.AuditActivity;
import it.andrea.start.models.support.AuditTypeOperation;

public class AuditTraceDTO implements Serializable {
    private static final long serialVersionUID = -3074992839717814645L;

    private Long id;
    private AuditActivity activity;
    private Long userId;
    private String userName;
    private AuditTypeOperation auditType;
    private LocalDateTime dateEvent;
    private String dateEventString;
    private String controllerMethod;
    private String entityName;
    private String entityKeyValue;
    private String entityOldValue;
    private String entityNewValue;
    private String method;
    private String url;
    private String httpContextRequest;
    private String httpContextResponse;
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

    public String getDateEventString() {
	return dateEventString;
    }

    public void setDateEventString(String dateEventString) {
	this.dateEventString = dateEventString;
    }

}
