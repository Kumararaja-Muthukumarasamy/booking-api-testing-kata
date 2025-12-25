package com.booking.client;

import static io.restassured.RestAssured.*;

import com.booking.constants.APIPaths;
import com.booking.model.AuthRequest;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

public class AuthClient {
    private AuthClient() {

    }

    public static Response generateToken(String username, String password) {
        AuthRequest request = new AuthRequest(username, password);
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body(request)
                .when()
                .post(APIPaths.AUTH_LOGIN);
    }
}