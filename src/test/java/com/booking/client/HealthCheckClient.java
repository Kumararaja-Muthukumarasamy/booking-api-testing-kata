package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.APIPaths;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class HealthCheckClient {
    private HealthCheckClient() {

    }

    public static Response getHealthStatus() {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .when()
                .get(ConfigReader.getProperty(ConfigKey.HEALTH_ENDPOINT));
    }
}
