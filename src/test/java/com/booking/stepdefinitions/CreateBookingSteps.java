package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class CreateBookingSteps {

    private Response response;
    private BookingRequest bookingRequest;

    @Given("the booking service is available")
    public void the_booking_service_is_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL), "Base URL must be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT), "Booking endpoint must be configured"
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
                .body(BookingResponseKeys.BOOKING_ID, greaterThan(0))
                .body(BookingResponseKeys.ROOM_ID, equalTo(bookingRequest.getRoomid()))
                .body(BookingResponseKeys.FIRSTNAME, equalTo(bookingRequest.getFirstname()))
                .body(BookingResponseKeys.LASTNAME, equalTo(bookingRequest.getLastname()))
                .body(BookingResponseKeys.DEPOSIT_PAID, equalTo(bookingRequest.isDepositpaid()))
                .body(BookingResponseKeys.CHECKIN, equalTo(bookingRequest.getBookingdates().getCheckin()))
                .body(BookingResponseKeys.CHECKOUT, equalTo(bookingRequest.getBookingdates().getCheckout()));
    }

    @When("I create a booking with missing {string}")
    public void i_create_a_booking_with_missing_field(String field) {
        BookingRequest request = BookingDataFactory.bookingWithMissingField(field);
        response = CreateBookingClient.createBooking(request);
    }

    @Then("the booking should fail with status {int}")
    public void the_booking_should_fail_with_status(int expectedStatus) {
        Assertions.assertEquals(expectedStatus, response.getStatusCode(),
                "Unexpected status code");
    }

    @Then("the error message should be {string}")
    public void the_error_message_should_be(String expectedMessage) {
        List<String> errors = response.jsonPath().getList("errors");
        Assertions.assertTrue(errors.contains(expectedMessage),
                "Expected error message not found. Actual: " + errors);
    }
}