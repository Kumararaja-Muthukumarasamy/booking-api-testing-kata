package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingDates;
import com.booking.model.BookingRequest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CreateBookingSteps {

    private Response response;

    @When("I create booking with valid details")
    public void i_create_booking_with_valid_details() {
        BookingDates dates = new BookingDates("2024-02-01", "2024-02-05");
        BookingRequest requestBody = new BookingRequest(
                555,
                "Johney",
                "Doego",
                true,
                dates,
                "johney.doego@example.com",
                "12345678902"
        );

        response = CreateBookingClient.createBooking(requestBody);
    }

    @Then("the booking should be created successfully")
    public void the_booking_should_be_created_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.CREATED)
                .body("bookingid", greaterThan(0))
                .body("roomid", equalTo(555))
                .body("firstname", equalTo("Johney"))
                .body("lastname", equalTo("Doego"))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2024-02-01"))
                .body("bookingdates.checkout", equalTo("2024-02-05"));
    }
}