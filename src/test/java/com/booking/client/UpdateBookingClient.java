package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class UpdateBookingClient {

    private static final Logger logger =
            LoggerUtil.getLogger(UpdateBookingClient.class);

    private UpdateBookingClient() {
    }

    public static Response updateBooking(
            int bookingId,
            Object requestBody,
            String token
    ) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);

        logger.info("Updating booking with ID {}", bookingId);

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", "token=" + token)
                .pathParam("id", bookingId)
                .body(requestBody)
                .when()
                .put(endpoint + "/{id}");

        logger.debug("Update booking response status: {}", response.getStatusCode());
        logger.debug("Update booking response body: {}", response.asString());

        return response;
    }
}