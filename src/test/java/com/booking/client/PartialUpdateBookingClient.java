package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.JsonLogUtil;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class PartialUpdateBookingClient {

    private static final Logger logger = LoggerUtil.getLogger(PartialUpdateBookingClient.class);

    private PartialUpdateBookingClient() {
    }

    private static String resolveTokenHeader(String token) {
        return token != null ? "token=" + token : "";
    }

    public static Response patchBooking(
            int bookingId,
            Object requestBody,
            String token
    ) {

        String endpoint =
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}";

        logger.info(
                "PATCH booking | bookingId={} | requestBody={}",
                bookingId,
                JsonLogUtil.toJson(requestBody)
        );

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", resolveTokenHeader(token))
                .pathParam("id", bookingId)
                .body(requestBody)
                .when()
                .patch(endpoint);

        logger.info(
                "PATCH booking response | bookingId={} | statusCode={}",
                bookingId,
                response.getStatusCode()
        );

        return response;
    }
}