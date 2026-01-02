package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.api.BookingResponseKeys;
import com.booking.constants.api.schema.SchemaPaths;
import com.booking.models.booking.BookingRequest;
import com.booking.testdata.booking.BookingTestDataFactory;
import com.booking.utils.auth.TokenFactory;
import com.booking.utils.context.ScenarioContext;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.validation.SchemaValidatorUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class CommonApiSteps {

    private static final Logger logger =
            LoggerUtil.getLogger(CommonApiSteps.class);

    // -------------------------------------------------
    // GIVEN
    // -------------------------------------------------

    @Given("the booking service is available")
    public void verify_booking_service_is_available() {

        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL),
                "Base URL must be configured"
        );

        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT),
                "Booking endpoint must be configured"
        );

        logger.info("Booking service configuration verified");
    }

    @Given("a booking exists")
    public void ensure_booking_exists() {
        BookingRequest bookingRequest = BookingTestDataFactory.validBooking();
        Response response = CreateBookingClient.createBooking(bookingRequest);
        Integer bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);

        Assertions.assertNotNull(
                bookingId, "Booking ID must be created for further API operations"
        );

        ScenarioContext.set("bookingId", bookingId);
        ScenarioContext.set("response", response);

        logger.info("Booking created successfully | bookingId={}", bookingId);
    }

    // -------------------------------------------------
    // THEN – Common Error Assertions
    // -------------------------------------------------

    @Then("the error message should be {string}")
    public void verify_error_message(String expectedMessage) {
        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response, "Response must be available before verifying error message"
        );

        String actualError = response.jsonPath().getString(BookingResponseKeys.ERROR);

        Assertions.assertEquals(
                expectedMessage, actualError, "Error message mismatch"
        );

        logger.info(
                "Verified error message | expected='{}' | actual='{}'",
                expectedMessage, actualError
        );
    }

    @Then("the error messages should be {string}")
    public void verify_error_messages(String expectedMessage) {
        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response, "Response must be available before verifying error messages"
        );

        String actualError =
                response.jsonPath().getString("errors[0]");

        if (actualError == null) {
            actualError = response.jsonPath().getString(BookingResponseKeys.ERROR);
        }

        Assertions.assertEquals(
                expectedMessage, actualError, "Validation error mismatch"
        );

        logger.info(
                "Verified validation error | expected='{}' | actual='{}'",
                expectedMessage, actualError
        );
    }

    // -------------------------------------------------
    // GIVEN – Token Handling
    // -------------------------------------------------

    @Given("I use a {string} token")
    public void configure_token(String tokenType) {

        String token =
                TokenFactory.resolveToken(tokenType);

        ScenarioContext.set("token", token);

        logger.info(
                "Configured token | type={} | state={}",
                tokenType,
                token == null
                        ? "MISSING"
                        : token.isEmpty()
                        ? "EMPTY"
                        : "PRESENT"
        );
    }
    @Then("the request should fail with status {int}")
    public void verify_request_failed_with_status(int expectedStatus) {

        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before verifying request failure"
        );

        int actualStatus = response.getStatusCode();
        String responseBody = response.getBody().asString();

        String errorMessage = null;

        // Defensive handling: only parse JSON if body exists
        if (responseBody != null && !responseBody.isBlank()) {
            try {
                errorMessage = response.jsonPath().getString("error");
            } catch (Exception e) {
                // Body is not JSON or has unexpected structure
                errorMessage = responseBody;
            }
        }

        logger.warn(
                "API request failed | expectedStatus={} | actualStatus={} | errorMessage={}",
                expectedStatus,
                actualStatus,
                errorMessage
        );

        logger.warn("Failure response body | {}", responseBody);

        // Status assertion must always run
        Assertions.assertEquals(
                expectedStatus,
                actualStatus,
                "Request failure status mismatch. Expected=" + expectedStatus +
                        ", Actual=" + actualStatus +
                        ". Response body: " + responseBody
        );
    }

    @Then("the error response should match the common error schema")
    public void validate_common_error_schema() {
        Response response = (Response) ScenarioContext.get("response");
        Assertions.assertNotNull(
                response,
                "Response must be available before validating error schema"
        );
        SchemaValidatorUtil.validateSchema(
                response, SchemaPaths.ERROR_RESPONSE_SCHEMA
        );

        logger.info("Validated error response against common error schema");
    }

    @Then("the request should not be successful")
    public void verify_request_not_successful() {
        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before verifying failure"
        );

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        logger.warn("API request failed as expected | status={}", statusCode);
        logger.warn("API response body           | {}", responseBody);

        Assertions.assertTrue(
                statusCode >= 400 && statusCode < 500,
                "Expected 4xx client error but got " + statusCode +
                        ". Response body: " + responseBody
        );
    }
}