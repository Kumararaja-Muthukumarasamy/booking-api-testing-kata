package com.booking.client;

import com.booking.constants.APIPaths;
import com.booking.spec.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateBookingClient {

    public static Response createBooking(Object bookingRequest) {
        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .log().all()
                .body(bookingRequest)
                .when()
                .post(APIPaths.CREATE_BOOKING);
    }
}