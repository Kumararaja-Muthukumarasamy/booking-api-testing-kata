package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.DeleteBookingClient;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import com.booking.utils.IdConverter;
import com.booking.utils.TokenFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeleteBookingSteps {
    private static final Logger logger = LogManager.getLogger(DeleteBookingSteps.class);

    private Response response;
    private int bookingId;
    private BookingRequest originalBooking;
    private String token;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("a valid booking exists for delete")
    public void a_valid_booking_exists_for_delete() throws JsonProcessingException {
        originalBooking = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(originalBooking);
        response.then().statusCode(HTTPStatusCodes.CREATED);

        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
        assertThat(bookingId, greaterThan(0));

        logger.info("Created booking with ID {}", bookingId);
    }

    @Given("I am authenticated with valid token for delete")
    public void i_am_authenticated_with_valid_token_for_delete() {
        token = TokenManager.getToken();
        assertThat(token, not(emptyString()));
        assertThat(token, notNullValue());

        logger.info("Retrieved valid token for delete");
    }

    @Given("I use a {string} token for delete")
    public void i_use_a_token_for_delete(String tokenType) {
        currentToken = TokenFactory.resolveToken(tokenType);
        logger.info("Configured {} token for delete", tokenType);
    }

    // ---------- WHEN ----------

    @When("I delete the booking")
    public void i_delete_the_booking() {
        response = DeleteBookingClient.deleteBooking(bookingId, token);
        logger.info("Sent DELETE request for booking ID {}", bookingId);
    }

    @When("I delete the booking using token")
    public void i_delete_the_booking_using_token() {
        response = DeleteBookingClient.deleteBooking(bookingId, currentToken);
        logger.info("Sent DELETE request for booking ID {} using configured token", bookingId);
    }

    @When("I delete the booking using id {string}")
    public void i_delete_the_booking_using_id(String idText) {
        int id = IdConverter.toIntOrDefault(idText, -1);
        response = DeleteBookingClient.deleteBooking(id, token);
        logger.info("Sent DELETE request with ID {}", id);
    }

    @When("I send delete request without id")
    public void i_send_delete_request_without_id() {
        response = DeleteBookingClient.deleteBookingWithoutId(token);
        logger.info("Sent DELETE request without ID");
    }

    // ---------- THEN ----------

    @Then("the booking should be deleted successfully")
    public void the_booking_should_be_deleted_successfully() {
        response.then().statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.SUCCESS, equalTo(true));
        logger.info("Booking deleted successfully");
    }

    @Then("the delete request should fail with status {int} and error message {string}")
    public void the_delete_request_should_fail_with_status_and_error_message(int statusCode, String errorMessage) {
        response.then().statusCode(statusCode)
                .body(BookingResponseKeys.ERROR, equalTo(errorMessage));
        logger.info("Delete failed with status {} and error '{}'", statusCode, errorMessage);
    }

    @Then("the delete request should fail with status {int}")
    public void the_delete_request_should_fail_with_status(int statusCode) {
        response.then().statusCode(statusCode);
        logger.info("Delete failed with status {}", statusCode);
    }
}