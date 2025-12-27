package com.booking.utils;

public class TokenFactory {
    private TokenFactory() {
    }

    public static String resolveToken(String tokenType) {
        return switch (tokenType.toLowerCase()) {
            case "missing" -> null;
            case "empty" -> "";
            case "invalid" -> "invalid_token_123";
            default -> throw new IllegalArgumentException("Unsupported token type: " + tokenType);
        };
    }
}

