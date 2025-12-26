package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CreateBookingSteps {

    private Response response;
    private BookingRequest bookingRequest;
    @Given("the booking service is available")
    public void the_booking_service_is_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL),"Base URL must be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT),"Booking endpoint must be configured"
        );
    }

    @When("I create booking with valid details")
    public void i_create_booking_with_valid_details() {
        bookingRequest = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
    }

    @Then("the booking should be created successfully")
    public void the_booking_should_be_created_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.CREATED)
                .body("bookingid", greaterThan(0))
                .body("roomid", equalTo(bookingRequest.getRoomid()))
                .body("firstname", equalTo(bookingRequest.getFirstname()))
                .body("lastname", equalTo(bookingRequest.getLastname()))
                .body("depositpaid", equalTo(bookingRequest.isDepositpaid()))
                .body("bookingdates.checkin",
                        equalTo(bookingRequest.getBookingdates().getCheckin()))
                .body("bookingdates.checkout",
                        equalTo(bookingRequest.getBookingdates().getCheckout()));
    }
}