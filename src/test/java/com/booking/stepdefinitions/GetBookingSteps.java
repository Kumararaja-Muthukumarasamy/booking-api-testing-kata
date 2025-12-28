package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(GetBookingSteps.class);

    private Response response;
    private int bookingId;
    private BookingRequest bookingRequest;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("the booking service for GetBookingByID is available")
    public void booking_service_available() {
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BASE_URL), "Base URL must be configured");
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT), "Booking endpoint must be configured");
        logger.info("Booking service for GetBookingByID is available");
    }

    @Given("a booking exists")
    public void booking_exists() {
        bookingRequest = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        response.then().statusCode(HTTPStatusCodes.CREATED);
        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
        logger.info("Created booking with ID {}", bookingId);
    }

    @Given("I am authenticated")
    public void authenticated() {
        currentToken = TokenManager.getToken();
        assertThat(currentToken, not(emptyString()));
        assertThat(currentToken, notNullValue());
        logger.info("Authenticated successfully with token");
    }

    @Given("I use a {string} token")
    public void use_token(String tokenType) {
        currentToken = TokenFactory.resolveToken(tokenType);
        logger.info("Using {} token for GetBookingByID", tokenType);
    }

    // ---------- WHEN ----------

    @When("I retrieve the booking by ID")
    public void retrieve_booking() {
        response = GetBookingClient.getBookingById(bookingId, currentToken);
        logger.info("Sent request to retrieve booking with ID {}", bookingId);
    }

    @When("I retrieve booking with ID {string}")
    public void retrieve_with_id(String bookingIdType) {
        String resolvedBookingId = BookingDataFactory.resolveBookingId(bookingIdType);
        response = GetBookingClient.getBookingById(resolvedBookingId, currentToken);
        logger.info("Sent request to retrieve booking with ID type '{}'", bookingIdType);
    }

    @When("I retrieve booking without ID")
    public void retrieve_without_id() {
        response = GetBookingClient.getBookingWithoutId(currentToken);
        logger.info("Sent request to retrieve booking without ID");
    }

    // ---------- THEN ----------

    @Then("the booking details should be returned successfully")
    public void booking_success() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.ROOM_ID, equalTo(bookingRequest.getRoomid()));
        logger.info("Booking details retrieved successfully for ID {}", bookingId);
    }

    @Then("the request should fail with status {int} and error message {string}")
    public void request_failed(int status, String error) {
        response.then()
                .statusCode(status)
                .body(BookingResponseKeys.ERROR, equalTo(error));
        logger.info("Request failed with status {} and error '{}'", status, error);
    }

    @Then("the request should fail with status {int}")
    public void request_failed_status(int status) {
        response.then().statusCode(status);
        logger.info("Request failed with status {}", status);
    }

    @Then("the get booking response should match the schema")
    public void validate_get_booking_response_schema() {
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.GET_BOOKING_RESPONSE_SCHEMA);
        logger.debug("Validated get booking response against {}", SchemaPaths.GET_BOOKING_RESPONSE_SCHEMA);
    }
}