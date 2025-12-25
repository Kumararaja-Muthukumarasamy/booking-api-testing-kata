package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CreateBookingSteps {

    private Response response;

    @When("I create booking with valid details")
    public void i_create_booking_with_valid_details() {
        BookingRequest requestBody = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(requestBody);
    }

    @Then("the booking should be created successfully")
    public void the_booking_should_be_created_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.CREATED)
                .body("bookingid", greaterThan(0))
                .body("roomid", equalTo(666))
                .body("firstname", equalTo("XJohn"))
                .body("lastname", equalTo("XDoe"))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2024-03-05"))
                .body("bookingdates.checkout", equalTo("2024-05-07"));
    }
}