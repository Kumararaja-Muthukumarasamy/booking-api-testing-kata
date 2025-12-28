package com.booking.stepdefinitions;

import com.booking.utils.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.client.BookingPatchClient;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingTestDataFactory;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.data.TokenFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookingPatchSteps {
    private static final Logger logger = LoggerUtil.getLogger(GetBookingClient.class);
    private Response response;
    private int bookingId;
    private BookingRequest originalBooking;
    private Map<String, Object> partialUpdate;
    private String token;
    private String currentToken;


    // ---------- GIVEN ----------

    @Given("a valid booking exists for patch")
    public void a_valid_booking_exists_for_patch() throws JsonProcessingException {
        originalBooking = BookingTestDataFactory.validBooking();
        response = CreateBookingClient.createBooking(originalBooking);
        response.then().statusCode(HTTPStatusCodes.CREATED);

        bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("=== ORIGINAL BOOKING PAYLOAD ===");
        System.out.println(new ObjectMapper().writeValueAsString(originalBooking));
        System.out.println("=== ORIGINAL CREATE RESPONSE ===");
        System.out.println(response.asString());

        assertThat(bookingId, greaterThan(0));
    }

    @Given("I am authenticated with valid token for patch")
    public void i_am_authenticated_with_valid_token_for_patch() {
        token = TokenManager.getToken();
        assertThat(token, not(emptyString()));
        assertThat(token, notNullValue());
    }

    // ---------- WHEN ----------

    @When("I patch the booking with partial details")
    public void i_patch_the_booking_with_partial_details() {
        partialUpdate = BookingTestDataFactory.getValidPatchData("roomid");
        partialUpdate.putAll(BookingTestDataFactory.getValidPatchData("firstname"));
        partialUpdate.putAll(BookingTestDataFactory.getValidPatchData("lastname"));
        partialUpdate.putAll(BookingTestDataFactory.getValidPatchData("depositpaid"));

       response = BookingPatchClient.patchBooking(bookingId, partialUpdate, token);
    }

    @When("I patch the booking with valid {string}")
    public void i_patch_the_booking_with_valid_field(String field) {
        partialUpdate = BookingTestDataFactory.getValidPatchData(field);
        response = BookingPatchClient.patchBooking(bookingId, partialUpdate, token);
    }

    @When("I patch the booking with invalid {string}")
    public void i_patch_the_booking_with_invalid_field(String field) {
        partialUpdate = BookingTestDataFactory.getInvalidPatchData(field);
        response = BookingPatchClient.patchBooking(bookingId, partialUpdate, token);
    }

    @When("I patch the booking with valid details using {string} token")
    public void i_patch_the_booking_with_valid_details_using_token(String tokenType) {
        partialUpdate = BookingTestDataFactory.getValidPatchData("firstname");
        partialUpdate.putAll(BookingTestDataFactory.getValidPatchData("lastname"));
        partialUpdate.putAll(BookingTestDataFactory.getValidPatchData("depositpaid"));

        // FIX: resolve tokenType into currentToken
        currentToken = TokenFactory.resolveToken(tokenType);

        response = BookingPatchClient.patchBooking(bookingId, partialUpdate, currentToken);
        logger.info("Sent PATCH request with {} token for booking ID {}", tokenType, bookingId);
    }


    // ---------- THEN ----------

    @Then("the booking should be partially updated successfully")
    public void the_booking_should_be_partially_updated_successfully() {
        response.then().statusCode(HTTPStatusCodes.OK);
        partialUpdate.forEach((key, value) -> response.then().body(key, equalTo(value)));
    }

    @Then("retrieving the booking should reflect patched details")
    public void retrieving_the_booking_should_reflect_patched_details() {
        Response getResponse = GetBookingClient.getBookingById(bookingId, TokenManager.getToken());
        getResponse.then().statusCode(HTTPStatusCodes.OK);

        partialUpdate.forEach((key, value) ->
                getResponse.then().body(key, equalTo(value))
        );

        logger.info("Verified patched booking details reflected correctly for ID {}", bookingId);
    }


    @Then("the patch request should fail with status {int} and error message {string}")
    public void the_patch_request_should_fail_with_status_and_error_message(int statusCode, String errorMessage) {
        response.then().statusCode(statusCode);

        String body = response.asString();
        if (body.contains("\"errors\"")) {
            response.then().body("errors", hasItem(errorMessage));
        } else {
            response.then().body("error", equalTo(errorMessage));
        }
    }
}