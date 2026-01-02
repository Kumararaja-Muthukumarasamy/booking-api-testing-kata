package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.constants.api.BookingResponseKeys;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.constants.api.schema.SchemaPaths;
import com.booking.models.booking.BookingRequest;
import com.booking.testdata.booking.BookingTestDataFactory;
import com.booking.utils.context.ScenarioContext;
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
import static org.hamcrest.Matchers.greaterThan;

public class CreateBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(CreateBookingSteps.class);
    private Response response;
    private Object requestPayload;
    private BookingRequest bookingRequest;
    private BookingRequest originalBookingRequest;

    // -------------------------------------------------
    // WHEN
    // -------------------------------------------------


    @When("I create a booking with all required fields")
    public void create_booking_with_all_required_fields() {
        bookingRequest = BookingTestDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        logger.info("Sent create booking request with all required fields");
    }

    @Given("a booking exists for the room")
    public void create_booking_for_duplicate_test() {
        originalBookingRequest = BookingTestDataFactory.validBooking();
        Response createResponse = CreateBookingClient.createBooking(originalBookingRequest);
        int bookingId = createResponse.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);

        Assertions.assertNotNull(
                bookingId, "Booking must be created before duplicate test"
        );

        logger.info("Initial booking created for duplicate test. BookingId={}, RoomId={}",
                bookingId, originalBookingRequest.getRoomid()
        );
    }

    @When("I create another booking for the same room")
    public void create_duplicate_booking() {
        response = CreateBookingClient.createBooking(originalBookingRequest);
        ScenarioContext.set("response", response);
        logger.info("Attempted duplicate booking with RoomId={}", originalBookingRequest.getRoomid());
    }

    @When("I create a booking with missing {string}")
    public void send_create_booking_request_with_missing_field(String field) {
        requestPayload = BookingTestDataFactory.bookingWithMissingField(field);
        response = CreateBookingClient.createBooking(requestPayload);
        logger.info("Sent create booking request missing field '{}'", field);
    }

    @When("I create a booking with invalid {string}")
    public void send_create_booking_request_with_invalid_field(String field) {
        Object requestPayload = BookingTestDataFactory.bookingWithInvalidField(field);
        response = CreateBookingClient.createBooking(requestPayload);
        logger.info("Sent create booking request invalid field '{}'", field);
    }

    @When("I create a booking with valid {string}")
    public void create_booking_with_valid_boundary_values(String field) {
        Object requestPayload = BookingTestDataFactory.bookingWithValidBoundary(field);
        response = CreateBookingClient.createBooking(requestPayload);
        logger.info("Sent create booking request valid boundaries '{}'", field);
    }

    // -------------------------------------------------
    // THEN
    // -------------------------------------------------

    @Then("the error messages should contain {string}")
    public void the_error_messages_should_contain(String expectedMessage) {

        Assertions.assertNotNull(
                response,
                "Response must be available before verifying error messages"
        );

        int statusCode = response.getStatusCode();

        String actualErrorMessage;
        List<String> errors = response.jsonPath().getList("errors");

        if (errors != null && !errors.isEmpty()) {
            actualErrorMessage = String.join(", ", errors);
        } else {
            actualErrorMessage = response.jsonPath().getString("error");
        }

        Assertions.assertNotNull(
                actualErrorMessage,
                "No error message present in response. StatusCode=" + statusCode
        );

        logger.warn(
                "Validation error received | statusCode={} | errorMessage={}",
                statusCode,
                actualErrorMessage
        );

        Assertions.assertTrue(
                actualErrorMessage.contains(expectedMessage),
                "Error message mismatch. "
                        + "StatusCode=" + statusCode
                        + ", Expected to contain='" + expectedMessage + "'"
                        + ", Actual='" + actualErrorMessage + "'"
        );
    }



    @Then("the booking should be created successfully")
    public void verify_booking_created_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.BOOKING_ID, greaterThan(0))
                .body(BookingResponseKeys.ROOM_ID, equalTo(bookingRequest.getClass()))
                .body(BookingResponseKeys.FIRSTNAME, equalTo(bookingRequest.getFirstname()))
                .body(BookingResponseKeys.LASTNAME, equalTo(bookingRequest.getLastname()))
                .body(BookingResponseKeys.DEPOSIT_PAID, equalTo(bookingRequest.isDepositpaid()))
                .body(BookingResponseKeys.CHECKIN, equalTo(bookingRequest.getBookingdates().getCheckin()))
                .body(BookingResponseKeys.CHECKOUT, equalTo(bookingRequest.getBookingdates().getCheckout()))
                .body(BookingResponseKeys.EMAIL, equalTo(bookingRequest.getEmail()))
                .body(BookingResponseKeys.PHONE, equalTo(bookingRequest.getPhone()));

        logger.info(
                "Booking created successfully as per contract with ID {}",
                response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID)
        );
    }

    @Then("the booking should fail with status {int}")
    public void verify_booking_failed_with_status(int expectedStatus) {

        Assertions.assertNotNull(
                response,
                "Response must be available before verifying failure status"
        );

        int actualStatus = response.getStatusCode();
        String responseBody = response.getBody().asString();

        logger.warn(
                "Booking API failure validation | expectedStatus={} | actualStatus={}",
                expectedStatus, actualStatus
        );
        logger.warn("Booking API failure response body | {}", responseBody);

        Assertions.assertEquals(
                expectedStatus,
                actualStatus,
                "Status code mismatch. Expected=" + expectedStatus +
                        ", Actual=" + actualStatus +
                        ". Response body: " + responseBody
        );
    }

    @Then("the booking should fail with a client validation error")
    public void booking_should_fail_with_client_validation_error() {

        Assertions.assertNotNull(
                response,
                "Response must be available before validating failure"
        );

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        logger.warn(
                "Booking date validation failure | statusCode={} | response={}",
                statusCode, responseBody
        );

        Assertions.assertTrue(
                statusCode == 400 || statusCode == 409,
                "Expected client validation error (400 or 409), but got "
                        + statusCode + ". Response body: " + responseBody
        );
    }

    // -------------------------------------------------
    // SCHEMA VALIDATION
    // -------------------------------------------------

    @Then("the create booking request should match the create-booking request schema")
    public void validate_create_booking_request_schema() {
        SchemaValidatorUtil.validateSchema(bookingRequest, SchemaPaths.BOOKING_REQUEST_SCHEMA);
        logger.info("Create booking request matches schema {}", SchemaPaths.BOOKING_REQUEST_SCHEMA);
    }

    @Then("the create booking response should match the create-booking response schema")
    public void validate_create_booking_response_schema() {
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.BOOKING_RESPONSE_SCHEMA);
        logger.info("Create booking response matches schema {}", SchemaPaths.BOOKING_RESPONSE_SCHEMA);
    }
}