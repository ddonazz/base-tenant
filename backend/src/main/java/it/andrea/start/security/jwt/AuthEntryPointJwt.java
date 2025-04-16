package it.andrea.start.security.jwt;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import it.andrea.start.controller.response.BadRequestResponse;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.utils.HelperString;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logErrorDetails(request, authException);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResourceBundle rb = ResourceBundle.getBundle("bundles.Messages", Locale.ITALIAN);

        BadRequestResponse badRequestResponse = new BadRequestResponse("Unauthorized", authException.getMessage(), rb.getString(ErrorCode.AUTHORIZEUSER_PASSWORD_WRONG.getCode()));
        response.getOutputStream().println(HelperString.toJson(badRequestResponse));
    }

    private void logErrorDetails(HttpServletRequest request, AuthenticationException authException) {
        LOG.error("Unauthorized access attempt at [{}]: {} - {}", request.getRequestURL(), request.getRequestURI(), authException.getMessage());
    }

}
