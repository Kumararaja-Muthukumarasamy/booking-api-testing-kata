package com.booking.stepdefinitions;

import com.booking.utils.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.constants.api.BookingResponseKeys;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.constants.schema.SchemaPaths;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingTestDataFactory;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.validation.SchemaValidatorUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class GetBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(GetBookingSteps.class);

    private Response response;
    private int bookingId;
    private BookingRequest bookingRequest;
    private String currentToken; // local token

    // ---------- GIVEN ----------

    @Given("a booking exists")
    public void create_booking_for_retrieval() {
        bookingRequest = BookingTestDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        response.then().statusCode(HTTPStatusCodes.CREATED);
        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
        logger.info("Created booking with ID {}", bookingId);
    }

    @Given("I am authenticated with valid token for get booking")
    public void authenticate_with_valid_token_for_get_booking() {
        currentToken = TokenManager.getToken(); // always fetch fresh
        logger.info("Retrieved valid token");
    }

    // ---------- WHEN ----------

    @When("I retrieve the booking by ID")
    public void send_get_booking_request_by_id() {
        response = GetBookingClient.getBookingById(bookingId, currentToken);
        logger.info("Sent request to retrieve booking with ID {}", bookingId);
    }

    @When("I retrieve booking with ID {string}")
    public void send_get_booking_request_with_id_type(String bookingIdType) {
        String resolvedBookingId = BookingTestDataFactory.resolveBookingId(bookingIdType);
        response = GetBookingClient.getBookingById(resolvedBookingId, currentToken);
        logger.info("Sent request to retrieve booking with ID type '{}'", bookingIdType);
    }

    @When("I retrieve booking without ID")
    public void send_get_booking_request_without_id() {
        response = GetBookingClient.getBookingWithoutId(currentToken);
        logger.info("Sent request to retrieve booking without ID");
    }

    // ---------- THEN ----------

    @Then("the booking details should be returned successfully")
    public void verify_booking_details_returned_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.ROOM_ID, equalTo(bookingRequest.getRoomid()));
        logger.info("Booking details retrieved successfully for ID {}", bookingId);
    }

    @Then("the request should fail with status {int}")
    public void verify_get_booking_failed_with_status(int status) {
        response.then().statusCode(status);
        logger.info("Request failed with status {}", status);
    }

    @Then("the request should fail with status {int} and error message {string}")
    public void verify_request_failed_with_status_and_error(int statusCode, String expectedMessage) {
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

    @Then("the get booking response should match the schema")
    public void validate_get_booking_response_schema() {
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.GET_BOOKING_RESPONSE_SCHEMA);
        logger.debug("Validated get booking response against {}", SchemaPaths.GET_BOOKING_RESPONSE_SCHEMA);
    }
 }