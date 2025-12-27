package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.client.UpdateBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateBookingSteps {

    private Response response;
    private int bookingId;
    private BookingRequest originalBooking;
    private BookingRequest updatedBooking;
    private String token;

    @Given("a valid booking exists")
    public void a_valid_booking_exists() throws JsonProcessingException {
        originalBooking = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(originalBooking);
        response.then().statusCode(HTTPStatusCodes.CREATED);

        bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("=== ORIGINAL BOOKING PAYLOAD ===");
        System.out.println(new ObjectMapper().writeValueAsString(originalBooking));
        System.out.println("=== ORIGINAL CREATE RESPONSE ===");
        System.out.println(response.asString());

        assertThat(bookingId, greaterThan(0));
    }

    @Given("I am authenticated with valid token")
    public void i_am_authenticated_with_valid_token() {
        token = TokenManager.getToken();
        assertThat(token, not(isEmptyOrNullString()));
    }

    // ---------- WHEN ----------

    @When("I update the booking with valid details")
    public void i_update_the_booking_with_valid_details() throws JsonProcessingException {
        updatedBooking = BookingDataFactory.validBooking();
        System.out.println("=== UPDATED BOOKING PAYLOAD ===");
        System.out.println(new ObjectMapper().writeValueAsString(updatedBooking));
        response = UpdateBookingClient.updateBooking(bookingId, updatedBooking, token);
        System.out.println("=== UPDATED RESPONSE ===");
        System.out.println(response.asString());

    }

    @When("I update the booking with missing {string}")
    public void i_update_the_booking_with_missing_field(String field) {
        // Build a booking request with the specified field missing
        updatedBooking = BookingDataFactory.bookingWithoutField(field);

        // Send the update request with the invalid payload
        response = UpdateBookingClient.updateBooking(
                bookingId,
                updatedBooking,
                token
        );
    }

    @When("I update the booking with invalid {string}")
    public void i_update_the_booking_with_invalid_field(String field) {
        // Build a booking request with the specified invalid field
        updatedBooking = BookingDataFactory.bookingWithInvalidField(field);

        // Send the update request with the invalid payload
        response = UpdateBookingClient.updateBooking(
                bookingId,
                updatedBooking,
                token
        );
    }


    // ---------- THEN ----------

    @Then("the booking should be updated successfully")
    public void booking_should_be_updated_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("success", equalTo(true));
    }

    @Then("retrieving the booking should reflect updated details")
    public void retrieving_booking_should_reflect_updated_details() {

        Response getResponse =
                GetBookingClient.getBookingByIdWithToken(
                        bookingId, TokenManager.getToken());

        System.out.println("=== RETRIEVED RESPONSE ===");
        System.out.println(getResponse.asString());

        getResponse.then()
                .statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.FIRSTNAME, equalTo(updatedBooking.getFirstname()))
                .body(BookingResponseKeys.LASTNAME, equalTo(updatedBooking.getLastname()))
                .body(BookingResponseKeys.DEPOSIT_PAID, equalTo(updatedBooking.isDepositpaid()))
                .body(BookingResponseKeys.CHECKIN, equalTo(updatedBooking.getBookingdates().getCheckin()))
                .body(BookingResponseKeys.CHECKOUT, equalTo(updatedBooking.getBookingdates().getCheckout()));

        System.out.println("=== COMPARISON ===");
        System.out.println("Original roomid: " + originalBooking.getRoomid());
        System.out.println("Updated roomid: " + updatedBooking.getRoomid());
        System.out.println("Retrieved roomid: " + getResponse.jsonPath().getInt(BookingResponseKeys.ROOM_ID));

    }
    @Then("the update request should fail with status {int} and error message {string}")
    public void the_update_request_should_fail_with_status_and_error_message(int statusCode, String errorMessage) {
        response.then().statusCode(statusCode);

        // Handle both "error" (string) and "errors" (list)
        String body = response.asString();
        if (body.contains("\"errors\"")) {
            response.then().body("errors", hasItem(errorMessage));
        } else {
            response.then().body("error", equalTo(errorMessage));
        }
    }

}