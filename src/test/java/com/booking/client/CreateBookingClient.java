package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.JsonLogUtil;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class CreateBookingClient {

    private static final Logger logger = LoggerUtil.getLogger(CreateBookingClient.class);

    private CreateBookingClient() {
    }

    public static Response createBooking(Object requestBody) {

        String endpoint =
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT);

        logger.info("Sending Create Booking request | endpoint={}", endpoint);
        logger.info(
                "Create Booking request body | {}",
                JsonLogUtil.toJson(requestBody)
        );

        Response response =
                given()
                        .spec(RequestSpecFactory.getBaseRequestSpec())
                        .body(requestBody)
                        .when()
                        .post(endpoint)
                        .then()
                        .extract()
                        .response();

        logger.info(
                "Create Booking response | statusCode={}",
                response.getStatusCode()
        );

        logger.info(
                "Create Booking response body | {}",
                response.asPrettyString()
        );

        return response;
    }
}