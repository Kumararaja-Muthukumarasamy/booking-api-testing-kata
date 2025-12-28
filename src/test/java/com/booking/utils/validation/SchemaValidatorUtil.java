package com.booking.utils.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;

public class SchemaValidatorUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private SchemaValidatorUtil() {}

    public static void validateSchema(Response response, String schemaPath) {
        assertThat(
                "Response does not match schema: " + schemaPath,
                response.asString(),
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
        );
    }

    public static void validateSchema(String json, String schemaPath) {
        assertThat(
                "JSON does not match schema: " + schemaPath,
                json,
                JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
        );
    }

    public static void validateSchema(Object requestObject, String schemaPath) {
        try {
            String json = mapper.writeValueAsString(requestObject);
            validateSchema(json, schemaPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate request schema: " + schemaPath, e);
        }
    }
}