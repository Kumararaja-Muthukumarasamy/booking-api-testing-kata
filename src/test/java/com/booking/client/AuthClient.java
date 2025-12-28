package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.model.AuthRequest;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final Logger logger = LoggerUtil.getLogger(AuthClient.class);

    private AuthClient() {
    }

    public static Response generateToken(String username, String password) {
        String endpoint = ConfigReader.getProperty(ConfigKey.AUTH_ENDPOINT);
        logger.info("Sending Auth request to {}", endpoint);

        AuthRequest request = new AuthRequest(username, password);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body(request)
                .when()
                .post(endpoint);

        logger.debug("Authentication response status: {}", response.getStatusCode());
        logger.debug("Authentication response body: {}", response.asString());
        return response;
    }
}