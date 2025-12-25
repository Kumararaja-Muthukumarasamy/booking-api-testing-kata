package com.booking.client;

import static io.restassured.RestAssured.*;

import com.booking.constants.APIPaths;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

public class AuthClient {
    private AuthClient(){

    }
    public static Response generateToken(String username, String password){
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body("{\"username\":\""+ username + "\","+
                       "\"password\": \"" + password + "\" }")
                .when()
                .post(APIPaths.AUTH_LOGIN);
    }
}