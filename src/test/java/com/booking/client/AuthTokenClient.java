package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.models.auth.AuthRequest;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class AuthTokenClient {
    private static final Logger logger = LoggerUtil.getLogger(AuthTokenClient.class);

    private AuthTokenClient() {
    }

    public static Response generateToken(AuthRequest requestBody) {
        String endpoint = ConfigReader.getProperty(ConfigKey.AUTH_ENDPOINT);
        logger.info("Sending Auth request to {}", endpoint);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body(requestBody)
                .when()
                .post(endpoint);

        logger.info("Authentication response status: {}", response.getStatusCode());
        return response;
    }
}