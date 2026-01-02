package com.booking.spec;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.utils.auth.TokenManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class RequestSpecFactory {

    private RequestSpecFactory() {}

    public static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(JSON)
                .build();
    }

    public static RequestSpecification getAuthenticatedRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(JSON)
                .addHeader("Cookie", "token=" + TokenManager.getToken())
                .build();
    }

    private static String getBaseUrl() {
        return ConfigReader.getProperty(ConfigKey.BASE_URL);
    }
}