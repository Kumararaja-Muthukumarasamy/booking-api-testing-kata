package com.booking.spec;

import com.booking.auth.TokenManager;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class RequestSpecFactory {
    private RequestSpecFactory() {

    }

    public static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(ConfigReader.getProperty(ConfigKey.BASE_URL))
                .build();
    }
    public static RequestSpecification getAuthenticatedSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getProperty(ConfigKey.BASE_URL))
                .addHeader("Cookie", "token=" + TokenManager.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }
}