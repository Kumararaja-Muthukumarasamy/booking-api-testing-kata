package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.client.UpdateBookingPutClient;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.constants.SchemaPaths;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import com.booking.utils.LoggerUtil;
import com.booking.utils.SchemaValidatorUtil;
import com.booking.utils.TokenFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class UpdateBookingPutSteps {

    private static final Logger logger = LoggerUtil.getLogger(UpdateBookingPutSteps.class);

    private Response response;
    private int bookingId;
    private BookingRequest originalBooking;
    private BookingRequest updatedBooking;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("a valid booking exists")
    public void create_valid_booking_for_update() {
        originalBooking = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(originalBooking);
        response.then().statusCode(HTTPStatusCodes.CREATED);

        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
        logger.info("Created booking with ID {}", bookingId);
    }

    @Given("I am authenticated with valid token for update")
    public void authenticate_with_valid_token() {
        currentToken = TokenManager.getToken();
        logger.info("Retrieved valid token for update");
    }

    @Given("I use a {string} token for update")
    public void configure_token_for_update(String tokenType) {
        currentToken = TokenFactory.resolveToken(tokenType);
        logger.info("Configured {} token for update", tokenType);
    }

    // ---------- WHEN ----------

    @When("I update the booking with valid details")
    public void send_update_booking_request_with_valid_details() {
        updatedBooking = BookingDataFactory.updatedBooking();
        response = UpdateBookingPutClient.updateBooking(bookingId, updatedBooking, currentToken);
        logger.info("Sent PUT request to update booking ID {}", bookingId);
    }

    @When("I update the booking with valid details using {string} token")
    public void send_update_booking_request_with_token(String tokenType) {
        updatedBooking = BookingDataFactory.updatedBooking();
        currentToken = TokenFactory.resolveToken(tokenType);
        response = UpdateBookingPutClient.updateBooking(bookingId, updatedBooking, currentToken);
        logger.info("Sent PUT request with {} token for booking ID {}", tokenType, bookingId);
    }

    @When("I update the booking with missing {string}")
    public void send_update_booking_request_with_missing_field(String field) {
        updatedBooking = BookingDataFactory.safeBookingWithMissingField(field);
        response = UpdateBookingPutClient.updateBooking(bookingId, updatedBooking, currentToken);
        logger.info("Sent PUT request with missing field {}", field);
    }

    @When("I update the booking with invalid {string}")
    public void send_update_booking_request_with_invalid_field(String field) {
        updatedBooking = BookingDataFactory.safeBookingWithInvalidField(field);
        response = UpdateBookingPutClient.updateBooking(bookingId, updatedBooking, currentToken);
        logger.info("Sent PUT request with invalid field {}", field);
    }

    @When("I update the booking with valid {string}")
    public void send_update_booking_request_with_valid_boundary_field(String field) {
        updatedBooking = BookingDataFactory.safeBookingWithValidBoundary(field);
        response = UpdateBookingPutClient.updateBooking(bookingId, updatedBooking, currentToken);
        logger.info("Sent PUT request with valid boundary field {}", field);
    }

    // ---------- THEN ----------

    @Then("the booking should be updated successfully")
    public void verify_booking_updated_successfully() {
        response.then().statusCode(HTTPStatusCodes.OK);
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.UPDATE_BOOKING_RESPONSE_SCHEMA);
        logger.debug("Validated update response against {}", SchemaPaths.UPDATE_BOOKING_RESPONSE_SCHEMA);
        logger.info("Booking updated successfully for ID {}", bookingId);
    }


    @Then("retrieving the booking should reflect updated details")
    public void verify_booking_reflects_updated_details() {
        Response getResponse = GetBookingClient.getBookingById(bookingId, currentToken);
        getResponse.then().statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.FIRSTNAME, equalTo(updatedBooking.getFirstname()))
                .body(BookingResponseKeys.LASTNAME, equalTo(updatedBooking.getLastname()))
                .body(BookingResponseKeys.DEPOSIT_PAID, equalTo(updatedBooking.isDepositpaid()))
                .body(BookingResponseKeys.CHECKIN, equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body(BookingResponseKeys.CHECKOUT, equalTo(updatedBooking.getBookingdates().getCheckout()));
        logger.info("Verified updated booking details reflected correctly for ID {}", bookingId);
    }

    @Then("the update request should fail with status {int} and error message {string}")
    public void verify_update_failed_with_status_and_error(int statusCode, String expectedMessage) {
        response.then().statusCode(statusCode);

        List<String> errors = response.jsonPath().getList("errors");
        String actualError;

        if (errors != null && !errors.isEmpty()) {

            actualError = errors.get(0);
        } else {
            actualError = response.jsonPath().getString("error");
        }

        Assertions.assertEquals(expectedMessage, actualError, "Validation error mismatch");
        logger.info("Update failed with status {} and error '{}'", statusCode, actualError);
    }

    @Then("the update request should match the request schema")
    public void validate_update_request_schema() {
        SchemaValidatorUtil.validateSchema(updatedBooking, SchemaPaths.UPDATE_BOOKING_REQUEST_SCHEMA);
        logger.debug("Validated update request against {}", SchemaPaths.UPDATE_BOOKING_REQUEST_SCHEMA);
    }

}