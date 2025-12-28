package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class GetBookingClient {

    private static final Logger logger = LoggerUtil.getLogger(GetBookingClient.class);

    private GetBookingClient() {
        // prevent instantiation
    }

    private static String resolveTokenHeader(String token) {
        return token != null ? "token=" + token : "";
    }

    public static Response getBookingById(Object bookingId, String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}";
        logger.info("Sending GET booking request for ID {} to {}", bookingId, endpoint);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", resolveTokenHeader(token))
                .pathParam("id", bookingId)
                .when()
                .get(endpoint);

        logger.debug("GetBookingById response status: {}", response.getStatusCode());
        logger.debug("GetBookingById response body: {}", response.asString());
        return response;
    }

    public static Response getBookingWithoutId(String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Sending GET booking request without ID to {}", endpoint);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", resolveTokenHeader(token))
                .when()
                .get(endpoint);

        logger.debug("GetBookingWithoutId response status: {}", response.getStatusCode());
        logger.debug("GetBookingWithoutId response body: {}", response.asString());
        return response;
    }
}