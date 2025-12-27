package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class DeleteBookingClient {

    private static final Logger logger =
            LoggerUtil.getLogger(DeleteBookingClient.class);

    private DeleteBookingClient() {
        // prevent instantiation
    }

    private static io.restassured.specification.RequestSpecification baseRequest(String token) {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", token == null ? "" : "token=" + token);
    }

    public static Response deleteBooking(int bookingId, String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Deleting booking with ID {}", bookingId);

        Response response = baseRequest(token)
                .pathParam("id", bookingId)
                .when()
                .delete(endpoint + "/{id}");

        logger.debug("Delete booking response status: {}", response.getStatusCode());
        return response;
    }

    public static Response deleteBookingWithoutId(String token) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Deleting booking without ID");

        Response response = baseRequest(token)
                .when()
                .delete(endpoint);

        logger.debug("Delete without ID response status: {}", response.getStatusCode());
        return response;
    }
}