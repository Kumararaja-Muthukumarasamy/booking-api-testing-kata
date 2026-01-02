package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.JsonLogUtil;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class RetrieveBookingClient {

    private static final Logger logger = LoggerUtil.getLogger(RetrieveBookingClient.class);

    private RetrieveBookingClient() {
    }

    private static String resolveTokenHeader(String token) {
        return token != null ? "token=" + token : "";
    }

    public static Response getBookingById(Object bookingId, String token) {

        String endpoint =
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}";

        logger.info(
                "GET booking request | bookingId={} | endpoint={}",
                bookingId,
                endpoint
        );

        Response response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", resolveTokenHeader(token))
                .pathParam("id", bookingId)
                .when()
                .get(endpoint);

        logger.info(
                "GET booking response | bookingId={} | statusCode={} | responseBody={}",
                bookingId,
                response.getStatusCode(),
                JsonLogUtil.toJson(response.asString())
        );

        return response;
    }
}