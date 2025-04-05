package it.andrea.start.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.constants.AuditLevel;
import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.models.BaseEntityLong;
import it.andrea.start.models.BaseEntityString;
import it.andrea.start.models.support.AuditActivity;
import it.andrea.start.models.support.AuditTypeOperation;
import it.andrea.start.security.service.JWTokenUserDetails;
import jakarta.servlet.http.HttpServletRequest;

public class HelperAudit {
    
    private final ObjectMapper objectMapper;

    public HelperAudit(ObjectMapper objectMapper) {
	super();
	this.objectMapper = objectMapper;
    }

    public static AuditTraceDTO getAuditControllerOperation(AuditLevel auditLevel, AuditActivity auditActivity, JWTokenUserDetails jWTokenUserDetails, AuditTypeOperation auditTypeOperation, HttpServletRequest httpServletRequest, Object body, String controllerMethod) {
	if (auditLevel == AuditLevel.NOTHING || auditLevel == AuditLevel.DATABASE) {
	    return null;
	}

	AuditTraceDTO auditTraceDTO = new AuditTraceDTO();
	auditTraceDTO.setActivity(auditActivity);
	auditTraceDTO.setDateEvent(LocalDateTime.now());
	auditTraceDTO.setAuditType(auditTypeOperation);
	auditTraceDTO.setControllerMethod(controllerMethod);

	if (jWTokenUserDetails != null) {
	    auditTraceDTO.setUserName(jWTokenUserDetails.getUsername());
	}

	setHttpRequestInfo(httpServletRequest, auditTraceDTO, body);

	return auditTraceDTO;
    }

    private static void setHttpRequestInfo(HttpServletRequest request, AuditTraceDTO auditTraceDTO, Object body) {
	auditTraceDTO.setMethod(Optional.ofNullable(request.getMethod()).orElse(""));
	auditTraceDTO.setUrl(request.getRequestURI());

	Map<String, Object> parametersMap = new HashMap<>();

	parametersMap.put("queryString", request.getQueryString());

	Map<String, String[]> paramMap = request.getParameterMap();
	parametersMap.put("parameterMap", paramMap.isEmpty() ? null : paramMap);

	Map<String, String> headers = new HashMap<>();
	Collections.list(request.getHeaderNames()).forEach(header -> headers.put(header, request.getHeader(header)));
	parametersMap.put("headers", headers);

	String ipCall = Optional.ofNullable(request.getHeader("X-Forwarded-For")).orElse(request.getRemoteAddr());
	parametersMap.put("ipCall", ipCall);

	String bodyContent = getRequestBody(request, body);
	parametersMap.put("body", bodyContent);

	auditTraceDTO.setHttpContextRequest(HelperString.toJson(parametersMap));
    }

    private static String getRequestBody(HttpServletRequest request, Object body) {
	if (request instanceof ContentCachingRequestWrapper cachingRequest) {
	    byte[] content = cachingRequest.getContentAsByteArray();
	    if (content.length > 0) {
		try {
		    return new String(content, request.getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
		    return "Error reading request body: " + e.getMessage();
		}
	    }
	}

	if (body instanceof String stringBody) {
	    return stringBody;
	} else if (body != null) {
	    return HelperString.toJson(body);
	}

	return null;
    }

    public static String getBody(HttpServletRequest request) throws UnsupportedEncodingException {
	ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
	byte[] content = wrappedRequest.getContentAsByteArray();
	return new String(content, wrappedRequest.getCharacterEncoding());
    }

    public static void auditControllerOperationAddResponseAndException(AuditTraceDTO auditTraceDTO, Object response, Exception ex) {
	if (auditTraceDTO == null) {
	    return;
	}
	auditTraceDTO.setHttpContextResponse(HelperString.toJson(response));

	if (ex != null) {
	    auditTraceDTO.setExceptionTrace(getStackTrace(ex));
	}
    }

    private static String getStackTrace(Exception ex) {
	try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
	    ex.printStackTrace(pw);
	    return sw.toString();
	} catch (Exception e) {
	    return "Failed to capture stack trace";
	}
    }

    public static void auditUnionsAndSettings(AuditTraceDTO auditTraceDTO, Collection<AuditTraceDTO> audits) {
	for (AuditTraceDTO item : audits) {
	    item.setActivity(auditTraceDTO.getActivity());
	    item.setUserId(auditTraceDTO.getUserId());
	    item.setUserName(auditTraceDTO.getUserName());
	    item.setAuditType(auditTraceDTO.getAuditType());
	}

	audits.add(auditTraceDTO);
    }

    public String getKeyLongValue(Long id) {
	ObjectNode rootNode = objectMapper.createObjectNode();
	rootNode.put("key", id);

	return HelperString.toJson(rootNode);
    }

    public String getKeyStringValue(String id) {
	ObjectNode rootNode = objectMapper.createObjectNode();
	rootNode.put("key", id);

	return HelperString.toJson(rootNode);
    }

    public String getKeyStringValue(Object id) {
	String jsonString = null;
	String jsonObject = HelperString.toJson(id);

	ObjectNode rootNode = objectMapper.createObjectNode();
	rootNode.put("key", jsonObject);

	jsonString = HelperString.toJson(rootNode);

	return jsonString;
    }

    public static <T extends BaseEntityLong> AuditTraceDTO generateAudit(AuditTraceDTO audit, GlobalConfig globalConfig, T entity, String entityName, String oldValue) {
	if (globalConfig.getAuditLevel() == AuditLevel.ALL || globalConfig.getAuditLevel() == AuditLevel.DATABASE) {
	    audit.setDateEvent(LocalDateTime.now());
	    HelperAudit helperAudit = new HelperAudit(new ObjectMapper()); 
	    String keyValue = helperAudit.getKeyLongValue(entity.getId());
	    audit.setEntityName(entityName);
	    audit.setEntityKeyValue(keyValue);
	    audit.setEntityOldValue(oldValue);
	    audit.setEntityNewValue(HelperString.toJson(entity));
	}
	
	return audit;
    }

    public static <T extends BaseEntityString> AuditTraceDTO generateAudit(AuditTraceDTO audit, GlobalConfig globalConfig, T entity, String entityName, String oldValue) {
	if (globalConfig.getAuditLevel() == AuditLevel.ALL || globalConfig.getAuditLevel() == AuditLevel.DATABASE) {
	    audit.setDateEvent(LocalDateTime.now());
	    HelperAudit helperAudit = new HelperAudit(new ObjectMapper()); 
	    String keyValue = helperAudit.getKeyStringValue(entity.getId());
	    audit.setEntityName(entityName);
	    audit.setEntityKeyValue(keyValue);
	    audit.setEntityOldValue(oldValue);
	    audit.setEntityNewValue(HelperString.toJson(entity));
	}
	
	return audit;
    }

}
