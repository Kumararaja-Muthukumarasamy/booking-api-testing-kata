package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.model.AuthRequest;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private AuthClient() {

    }

    public static Response generateToken(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body(request)
                .when()
                .post(ConfigReader.getProperty(ConfigKey.AUTH_ENDPOINT));
    }
}