package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetBookingClient {

    private GetBookingClient() {
    }

    public static Response getBookingByIdWithToken(int bookingId, String token) {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", token != null ? "token=" + token : "")
                .pathParam("id", bookingId)
                .when()
                .get(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}");
    }

    public static Response getBookingByIdWithToken(String bookingId, String token) {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", token != null ? "token=" + token : "")
                .pathParam("id", bookingId)
                .when()
                .get(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}");
    }

    public static Response getBookingWithoutId(String token) {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .header("Cookie", token != null ? "token=" + token : "")
                .when()
                .get(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT));
    }
}
