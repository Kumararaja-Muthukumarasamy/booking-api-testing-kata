package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.model.BookingRequest;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class CreateBookingClient {
    private static final Logger logger = LoggerUtil.getLogger(CreateBookingClient.class);
    private CreateBookingClient() {

    }
    public static Response createBooking(BookingRequest bookingRequest) {
        String endpoint = ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);
        logger.info("Sending Create Booking request to {}", endpoint);
        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .body(bookingRequest)
                .when()
                .post(endpoint);
        logger.debug("Create Booking response status: {}", response.getStatusCode());
        logger.debug("Create Booking response body: {}", response.asString());
        return response;
    }
}