package com.booking.auth;

public final class TokenManager {
    private static String token;

    private TokenManager() {

    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String authToken) {
        token = authToken;
    }

    public static boolean isTokenAvailable() {
        return token != null && !token.isEmpty();
    }
}