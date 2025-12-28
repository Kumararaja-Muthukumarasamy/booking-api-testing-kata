package com.booking.utils.context;

public class ApiContext {

    private static final ThreadLocal<String> REQUEST = new ThreadLocal<>();
    private static final ThreadLocal<String> RESPONSE = new ThreadLocal<>();

    public static void setRequest(String request) {
        REQUEST.set(request);
    }

    public static void setResponse(String response) {
        RESPONSE.set(response);
    }

    public static String getRequest() {
        return REQUEST.get();
    }

    public static String getResponse() {
        return RESPONSE.get();
    }

    public static void clear() {
        REQUEST.remove();
        RESPONSE.remove();
    }
}