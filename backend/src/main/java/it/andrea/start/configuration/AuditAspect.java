package it.andrea.start.configuration;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import it.andrea.start.annotation.Audit;
import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditLevel;
import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.service.audit.AuditTraceService;
import it.andrea.start.utils.HelperAudit;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditAspect {

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

    @Around(
        value = "auditPointcut(auditAnnotation)",
        argNames = "joinPoint,auditAnnotation"
    )
    public Object handleAudit(ProceedingJoinPoint joinPoint, Audit auditAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = getCurrentHttpRequest();

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
            auditTrace.setHttpMethod("N/A");
            auditTrace.setRequestUri("N/A");
        }

        Object result;

        try {
            result = joinPoint.proceed();
            auditTrace.setSuccess(true);
        } catch (Throwable ex) {

            auditTrace.setSuccess(false);
            auditTrace.setActivity(AuditActivity.USER_OPERATION_EXCEPTION);

            auditTrace.setExceptionType(ex.getClass().getName());
            auditTrace.setExceptionMessage(ex.getMessage());

            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            auditTrace.setDurationMs(duration);

            if (shouldLog(currentLevel, auditTrace.getSuccess())) {
                auditTraceService.saveLog(auditTrace);
            }
        }

        return result;
    }

    private boolean shouldLog(AuditLevel level, boolean success) {
        return switch (level) {
            case ALL -> true;
            case ERRORS_ONLY -> !success;
            case SUCCESS_ONLY -> success;
            default -> false;
        };
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }

}