package it.andrea.start.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class HelperAudit {

    private static final Logger log = LoggerFactory.getLogger(HelperAudit.class);
    private static final int MAX_BODY_LENGTH = 2048;

    private final ObjectMapper objectMapper;

    public HelperAudit(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    public String getUserAgent(HttpServletRequest request) {
        if (request == null)
            return null;
        return request.getHeader("User-Agent");
    }

    public String formatParameters(Map<String, String[]> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty()) {
            return null;
        }
        try {
            Map<String, String> flattenedParams = parameterMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.join(",", entry.getValue())));
            return objectMapper.writeValueAsString(flattenedParams);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert parameters to JSON", e);
            return "{\"error\":\"Failed to serialize parameters\"}";
        }
    }

    public String getSanitizedRequestBody(HttpServletRequest request, Object[] args) {
        String bodyContent = null;

        if (request instanceof ContentCachingRequestWrapper cachingRequest) {
            byte[] content = cachingRequest.getContentAsByteArray();
            if (content.length > 0) {
                try {
                    bodyContent = new String(content, request.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    log.error("Error reading cached request body encoding", e);
                    bodyContent = "[Encoding Error]";
                }
            }
        } else if (args != null) {
            bodyContent = Arrays.stream(args)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(this::convertObjectToJsonSafe)
                    .orElse(null);

            if (bodyContent == null) {
                log.warn("Request is not a ContentCachingRequestWrapper, consider adding the filter. Body might be missing from audit.");
            }
        }

        if (bodyContent != null) {
            bodyContent = sanitize(bodyContent);
            if (bodyContent.length() > MAX_BODY_LENGTH) {
                bodyContent = bodyContent.substring(0, MAX_BODY_LENGTH) + "... [truncated]";
            }
        }

        return bodyContent;
    }

    private String convertObjectToJsonSafe(Object obj) {
        if (obj == null)
            return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert object to JSON for audit log: {}", e.getMessage());
            return "[Serialization Error]";
        }
    }

    private String sanitize(String content) {
        if (content == null)
            return null;
        content = content.replaceAll("(\"password\"\\s*:\\s*\")[^\"]*(\")", "$1********$2");
        content = content.replaceAll("(\"pass\"\\s*:\\s*\")[^\"]*(\")", "$1********$2");
        content = content.replaceAll("(\"secret\"\\s*:\\s*\")[^\"]*(\")", "$1********$2");
        return content;
    }

}