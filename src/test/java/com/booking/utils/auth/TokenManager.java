package com.booking.auth;

import com.booking.client.AuthClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.api.HTTPStatusCodes;

public final class TokenManager {

    private static String token;

    private TokenManager() {
    }

    public static String getToken() {
        if (token == null || token.isBlank()) {
            token = generateToken();
        }
        return token;
    }

    private static String generateToken() {
        return AuthClient.generateToken(
                        ConfigReader.getProperty(ConfigKey.AUTH_USERNAME),
                        ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD)
                )
                .then()
                .statusCode(HTTPStatusCodes.OK)
                .extract()
                .path("token");
    }

    public static void clearToken() {
        token = null;
    }
}
