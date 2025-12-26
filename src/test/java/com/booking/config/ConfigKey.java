package com.booking.config;

public enum ConfigKey {
    BASE_URL("base.url"),
    HEALTH_ENDPOINT("health.endpoint"),
    AUTH_ENDPOINT("auth.endpoint"),
    BOOKING_ENDPOINT("booking.endpoint"),

    AUTH_USERNAME("auth.username"),
    AUTH_PASSWORD("auth.password");

    private final String key;

    ConfigKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }
}
