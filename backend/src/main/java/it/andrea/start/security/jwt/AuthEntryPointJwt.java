package it.andrea.start.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.andrea.start.controller.response.ApiError;
import it.andrea.start.error.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logErrorDetails(request, authException);

        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
        HttpStatus status = errorCode.getHttpStatus();

        String errorMessage = messageSource.getMessage(
                errorCode.getCode(),
                null,
                "Authentication failed. Please check your credentials or token.",
                LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(
                status,
                errorCode,
                errorMessage,
                request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(apiError));
        response.getWriter().flush();
    }

    private void logErrorDetails(HttpServletRequest request, AuthenticationException authException) {
        LOG.error("Authentication Failed for request [{} {}]: Status={}, ErrorCode={}, Message='{}'",
                request.getMethod(),
                request.getRequestURI(),
                ErrorCode.AUTHENTICATION_FAILED.getHttpStatus().value(),
                ErrorCode.AUTHENTICATION_FAILED.getCode(),
                authException.getMessage());
        LOG.debug("AuthenticationException details:", authException);
    }

}
