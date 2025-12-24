package com.booking.spec;

import com.booking.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class RequestSpecFactory {
    private RequestSpecFactory() {

    }

    public static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(ConfigReader.getBaseUrl())
                .build();
    }
}