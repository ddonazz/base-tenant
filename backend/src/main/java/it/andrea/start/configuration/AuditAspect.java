package it.andrea.start.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.andrea.start.annotation.Audit;
import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.models.support.AuditActivity;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.service.audit.AuditTraceService;
import it.andrea.start.utils.HelperAudit;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditAspect {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditTraceService auditTraceService;
    private final GlobalConfig globalConfig;

    public AuditAspect(AuditTraceService auditTraceService, GlobalConfig globalConfig) {
	this.auditTraceService = auditTraceService;
	this.globalConfig = globalConfig;
    }
    
    @Pointcut("@annotation(audit)")
    public void auditPointcut(Audit audit) {}  

    @Around("auditPointcut(audit)")
    public Object handleAudit(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
	HttpServletRequest request = getCurrentHttpRequest();

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

	AuditTraceDTO auditAPICall = HelperAudit.getAuditControllerOperation(
		globalConfig.getAuditLevel(), 
		audit.activity(), 
		userDetails, 
		audit.type(), 
		request, 
		joinPoint.getArgs(), 
		joinPoint.getSignature().toShortString());

	try {
	        Object result = joinPoint.proceed();

	        if (result instanceof Pair<?, ?> pairResult) {
	            Object responsePayload = pairResult.getLeft();
	            Object serviceAudit = pairResult.getRight();

	            HelperAudit.auditControllerOperationAddResponseAndException(auditAPICall, responsePayload, null);

	            List<AuditTraceDTO> allAudits = new ArrayList<>();
	            allAudits.add(auditAPICall);
	            if (serviceAudit instanceof Collection<?> collection) {
	                for (Object obj : collection) {
	                    if (obj instanceof AuditTraceDTO auditTrace) {
	                        allAudits.add(auditTrace);
	                    }
	                }
	            }
	            auditTraceService.saveAuditTrace(allAudits);
	        } else {
	            HelperAudit.auditControllerOperationAddResponseAndException(auditAPICall, result, null);
	            auditTraceService.saveAuditTrace(List.of(auditAPICall));
	        }
	        return result;
	    } catch (Exception ex) {
	        auditAPICall.setActivity(AuditActivity.USER_OPERATION_EXCEPTION);
	        HelperAudit.auditControllerOperationAddResponseAndException(auditAPICall, null, ex);
	        auditTraceService.saveAuditTrace(List.of(auditAPICall));
	        throw ex;
	    }
    }
    
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

}
