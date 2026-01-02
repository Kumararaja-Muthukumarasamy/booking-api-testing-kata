package com.booking.utils.auth;

import com.booking.client.AuthTokenClient;
import com.booking.models.auth.AuthRequest;
import com.booking.testdata.auth.AuthTestDataFactory;
import io.restassured.response.Response;

public final class TokenManager {

    private static String token;

    private TokenManager() {
    }

    public static String getToken() {

        if (token == null) {
            token = generateToken();
        }
        return token;
    }

    private static String generateToken() {
        AuthRequest request = AuthTestDataFactory.createValidAuthRequest();
        Response response = AuthTokenClient.generateToken(request);

        return response.jsonPath().getString("token");
    }

    public static void clearToken() {
        token = null;
    }
}
