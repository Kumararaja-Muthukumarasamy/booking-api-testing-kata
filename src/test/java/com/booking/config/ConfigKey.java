package com.booking.config;

public enum ConfigKey {
    // Base
    BASE_URL("base.url"),

    // Endpoints
    HEALTH_ENDPOINT("health.endpoint"),
    AUTH_ENDPOINT("auth.endpoint"),
    BOOKING_ENDPOINT("booking.endpoint"),

    // Auth
    AUTH_USERNAME("auth.username"),
    AUTH_PASSWORD("auth.password");

    private final String key;

    ConfigKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}