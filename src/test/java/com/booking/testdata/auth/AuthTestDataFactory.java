package com.booking.testdata.auth;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.models.auth.AuthRequest;

public final class AuthTestDataFactory {
    private AuthTestDataFactory() {
    }
    public static AuthRequest createValidAuthRequest() {
        return new AuthRequest( ConfigReader.getProperty(ConfigKey.AUTH_USERNAME),
                ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD));
    }

    public static AuthRequest createInvalidAuthRequest(String scenario) {

        return switch (scenario) {

            case "InvalidUsername" -> new AuthRequest("wrong", ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD));

            case "InvalidPassword" -> new AuthRequest(ConfigReader.getProperty(ConfigKey.AUTH_USERNAME), "wrong");

            case "BothInvalid" -> new AuthRequest("wrong", "wrong");

            case "BlankUsername" -> new AuthRequest("", ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD));

            case "BlankPassword" -> new AuthRequest(ConfigReader.getProperty(ConfigKey.AUTH_USERNAME), "");

            case "MissingUsername" -> new AuthRequest(null, ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD));

            case "MissingPassword" -> new AuthRequest(ConfigReader.getProperty(ConfigKey.AUTH_USERNAME), null);

            default -> throw new IllegalArgumentException(
                    "Unsupported auth negative scenario: " + scenario);
        };
    }
}