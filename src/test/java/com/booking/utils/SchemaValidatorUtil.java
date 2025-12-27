package com.booking.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;

public class SchemaValidatorUtil {

    private SchemaValidatorUtil() {}

    public static void validateSchema(Response response, String schemaPath) {
        MatcherAssert.assertThat(
                "Response does not match schema: " + schemaPath,
                response.asString(),
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
        );
    }

    public static void validateSchema(String json, String schemaPath) {
        MatcherAssert.assertThat(
                "JSON does not match schema: " + schemaPath,
                json,
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
        );
    }
}