package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.stepdefinitions.HealthCheckSteps;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import static io.restassured.RestAssured.given;

public class HealthCheckClient {
    private static final Logger logger = LoggerUtil.getLogger(HealthCheckSteps.class);

    private HealthCheckClient() {

    }

    public static Response getHealthStatus() {
        String endpoint = ConfigReader.getProperty(ConfigKey.HEALTH_ENDPOINT);
        logger.info("Sending Health Check request to {}", endpoint);
        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .when()
                .get(endpoint);
        logger.debug("Health Check response body: {}", response.asString());
        return response;
    }
}