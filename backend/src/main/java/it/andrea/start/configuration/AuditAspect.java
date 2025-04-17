package it.andrea.start.configuration;

import it.andrea.start.annotation.Audit;
import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditLevel;
import it.andrea.start.models.BaseEntityLong;
import it.andrea.start.models.BaseEntityString;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.service.audit.AuditTraceService;
import it.andrea.start.utils.HelperAudit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditTraceService auditTraceService;
    private final GlobalConfig globalConfig;
    private final HelperAudit helperAudit;

    public AuditAspect(AuditTraceService auditTraceService, GlobalConfig globalConfig, HelperAudit helperAudit) {
        this.auditTraceService = auditTraceService;
        this.globalConfig = globalConfig;
        this.helperAudit = helperAudit;
    }

    @Pointcut("@annotation(auditAnnotation)")
    public void auditPointcut(Audit auditAnnotation) {
    }

    @Around("auditPointcut(auditAnnotation)")
    public Object handleAudit(ProceedingJoinPoint joinPoint, Audit auditAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = getCurrentHttpRequest();
        HttpServletResponse response = getCurrentHttpResponse();

        AuditLevel currentLevel = globalConfig.getAuditLevel();
        if (currentLevel == AuditLevel.NOTHING) {
            return joinPoint.proceed();
        }

        AuditTrace auditTrace = new AuditTrace();
        auditTrace.setDateEvent(LocalDateTime.now());
        auditTrace.setActivity(auditAnnotation.activity());
        auditTrace.setAuditType(auditAnnotation.type());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JWTokenUserDetails userDetails) {
            auditTrace.setUsername(userDetails.getUsername());
        } else {
            auditTrace.setUsername("anonymous");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        auditTrace.setClassName(signature.getDeclaringTypeName());
        auditTrace.setMethodName(signature.getName());
        auditTrace.setControllerMethod(signature.toShortString());

        if (request != null) {
            auditTrace.setHttpMethod(request.getMethod());
            auditTrace.setRequestUri(request.getRequestURI());
            auditTrace.setClientIpAddress(helperAudit.getClientIpAddress(request));
            auditTrace.setUserAgent(helperAudit.getUserAgent(request));
            auditTrace.setRequestParams(helperAudit.formatParameters(request.getParameterMap()));
            auditTrace.setRequestBody(helperAudit.getSanitizedRequestBody(request, joinPoint.getArgs()));
        } else {
            log.warn("HttpServletRequest not available for audit trace on method: {}", signature.toShortString());
            auditTrace.setHttpMethod("N/A");
            auditTrace.setRequestUri("N/A");
        }

        Object result = null;
        Throwable exception = null;

        try {
            result = joinPoint.proceed();
            auditTrace.setSuccess(true);
        } catch (Throwable ex) {
            exception = ex;
            auditTrace.setSuccess(false);
            auditTrace.setActivity(AuditActivity.USER_OPERATION_EXCEPTION);
            auditTrace.setExceptionTrace(helperAudit.getStackTrace(ex));
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            auditTrace.setDurationMs(duration);

            if (response != null) {
                auditTrace.setHttpStatus(response.getStatus());
                if (exception != null && response.getStatus() < 400) {
                    auditTrace.setHttpStatus(500);
                }
            } else if (exception != null) {
                auditTrace.setHttpStatus(500);
            }

            auditTrace.setResourceId(extractResourceId(result));

            if (currentLevel == AuditLevel.ALL) {
                auditTraceService.saveLog(auditTrace);
            }
        }
        return result;
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

    private HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getResponse() : null;
    }

    private String extractResourceId(Object result) {
        if (result instanceof BaseEntityLong baseEntityLong) {
            return baseEntityLong.getId().toString();
        } else if (result instanceof BaseEntityString baseEntityString) {
            return baseEntityString.getId();
        }
        return null;
    }
}