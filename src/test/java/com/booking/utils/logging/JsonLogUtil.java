package com.booking.utils.logging;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonLogUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonLogUtil() {
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }
}