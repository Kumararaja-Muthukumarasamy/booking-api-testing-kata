package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class DeleteBookingClient {

    private static final Logger logger = LoggerUtil.getLogger(DeleteBookingClient.class);

    private DeleteBookingClient() {

    }
    public static Response deleteBooking(int bookingId, String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Sending DELETE request for booking ID {} to {}", bookingId, endpoint);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", token != null ? "token=" + token : "")
                .pathParam("id", bookingId)
                .when()
                .delete(endpoint + "/{id}");

        logger.debug("Delete booking response status: {}", response.getStatusCode());
        logger.debug("Delete booking response body: {}", response.asString());
        return response;
    }

    public static Response deleteBookingWithoutId(String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Sending DELETE request without ID to {}", endpoint);

        Response response = given()
                .spec(token != null ? RequestSpecFactory.getAuthenticatedRequestSpec()
                        : RequestSpecFactory.getBaseRequestSpec())
                .when()
                .delete(endpoint);

        logger.debug("Delete without ID response status: {}", response.getStatusCode());
        return response;
    }
}