package it.andrea.start.utils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class HelperString {

    private static final Logger LOG = LoggerFactory.getLogger(HelperString.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Random random = new SecureRandom();

    private HelperString() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String toJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize object to JSON", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        if (StringUtils.isBlank(json) || clz == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clz);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to deserialize JSON to object", e);
            return null;
        }
    }

    public static <T> List<T> fromJsonList(String json, Class<T> convertType) {
        if (StringUtils.isBlank(json) || convertType == null) {
            return Collections.emptyList();
        }
        try {
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, convertType);
            return objectMapper.readValue(json, collectionType);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to deserialize JSON to list", e);
            return Collections.emptyList();
        }
    }

    public static String leftPaddingZeros(long number, int numZeros) {
        return StringUtils.leftPad(String.valueOf(number), numZeros, '0');
    }

    public static String randomString(String symbols, int length) {
        if (StringUtils.isBlank(symbols) || length < 1) {
            return "";
        }
        char[] byteSymbols = symbols.toCharArray();
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = byteSymbols[random.nextInt(byteSymbols.length)];
        }
        return new String(buf);
    }

}
