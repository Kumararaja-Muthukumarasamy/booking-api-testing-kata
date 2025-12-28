package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.constants.SchemaPaths;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import com.booking.utils.LoggerUtil;
import com.booking.utils.SchemaValidatorUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CreateBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(CreateBookingSteps.class);
    private Response response;
    private BookingRequest bookingRequest;

    // ---------- GIVEN ----------

    @Given("a booking already exists for a room")
    public void create_existing_booking_for_room() {
        bookingRequest = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        response.then().statusCode(HTTPStatusCodes.CREATED);
        logger.info("Created initial booking for duplicate room test");
    }

    // ---------- WHEN ----------

    @When("I create booking with valid details")
    public void send_create_booking_request_with_valid_details() {
        bookingRequest = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Sent create booking request with valid details");
    }

    @When("I create a booking with missing {string}")
    public void send_create_booking_request_with_missing_field(String field) {
        bookingRequest = BookingDataFactory.safeBookingWithMissingField(field);
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Sent create booking request missing field '{}'", field);
    }

    @When("I create another booking for the same room")
    public void send_create_booking_request_for_duplicate_room() {
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Attempted duplicate booking for room {}", bookingRequest.getRoomid());
    }

    @When("I create a booking with invalid {string}")
    public void send_create_booking_request_with_invalid_field(String field) {
        bookingRequest = BookingDataFactory.safeBookingWithInvalidField(field);
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Sent create booking request with invalid field '{}'", field);
    }

    @When("I create a booking with valid {string}")
    public void send_create_booking_request_with_valid_boundary_field(String field) {
        bookingRequest = BookingDataFactory.safeBookingWithValidBoundary(field);
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Sent create booking request with valid boundary field '{}'", field);
    }

    // ---------- THEN ----------

    @Then("the booking should be created successfully")
    public void verify_booking_created_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.CREATED)
                .body(BookingResponseKeys.BOOKING_ID, greaterThan(0))
                .body(BookingResponseKeys.ROOM_ID, equalTo(bookingRequest.getRoomid()))
                .body(BookingResponseKeys.FIRSTNAME, equalTo(bookingRequest.getFirstname()))
                .body(BookingResponseKeys.LASTNAME, equalTo(bookingRequest.getLastname()))
                .body(BookingResponseKeys.DEPOSIT_PAID, equalTo(bookingRequest.isDepositpaid()))
                .body(BookingResponseKeys.CHECKIN, equalTo(bookingRequest.getBookingdates().getCheckin()))
                .body(BookingResponseKeys.CHECKOUT, equalTo(bookingRequest.getBookingdates().getCheckout()));
        logger.info("Booking created successfully with ID {}", response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID));
    }

    @Then("the booking should fail with status {int}")
    public void verify_booking_failed_with_status(int expectedStatus) {
        Assertions.assertEquals(expectedStatus, response.getStatusCode(), "Unexpected status code");
        logger.info("Booking failed as expected with status {}", expectedStatus);
    }

    @Then("the error message should be {string}")
    public void verify_error_message(String expectedMessage) {
        String actualError = response.jsonPath().getString(BookingResponseKeys.ERROR);
        Assertions.assertEquals(expectedMessage, actualError, "Error message mismatch");
        logger.info("Received expected error message '{}'", expectedMessage);
    }

    @Then("the error messages should be {string}")
    public void verify_error_messages(String expectedMessage) {
        String actualError = response.jsonPath().getString("errors[0]");
        if (actualError == null) {
            actualError = response.jsonPath().getString(BookingResponseKeys.ERROR);
        }
        Assertions.assertEquals(expectedMessage, actualError, "Validation error mismatch");
        logger.info("Validation failed with expected message '{}'", expectedMessage);
    }

    @Then("the create booking request should match the schema")
    public void validate_create_booking_request_schema(){
        SchemaValidatorUtil.validateSchema(bookingRequest, SchemaPaths.CREATE_BOOKING_REQUEST_SCHEMA);
        logger.debug("Validated create booking request against {}", SchemaPaths.CREATE_BOOKING_REQUEST_SCHEMA);
    }

    @Then("the create booking response should match the schema")
    public void validate_create_booking_response_schema() {
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.CREATE_BOOKING_RESPONSE_SCHEMA);
        logger.debug("Validated create booking response against {}", SchemaPaths.CREATE_BOOKING_RESPONSE_SCHEMA);
    }
}