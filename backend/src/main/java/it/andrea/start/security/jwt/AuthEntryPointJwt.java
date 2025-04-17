package it.andrea.start.security.jwt;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.utils.HelperString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final MessageSource messageSource;

    public AuthEntryPointJwt(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logErrorDetails(request, authException);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = messageSource.getMessage(
                ErrorCode.AUTHORIZEUSER_USERNOTFOUND.getCode(),
                null,
                "Errore di autenticazione predefinito",
                LocaleContextHolder.getLocale()
        );

        BadRequestResponse badRequestResponse = new BadRequestResponse("Unauthorized", errorMessage);
        response.getOutputStream().println(HelperString.toJson(badRequestResponse));
    }

    private void logErrorDetails(HttpServletRequest request, AuthenticationException authException) {
        LOG.error("Unauthorized access attempt at [{}]: {} - {}", request.getRequestURL(), request.getRequestURI(), authException.getMessage());
    }

}
