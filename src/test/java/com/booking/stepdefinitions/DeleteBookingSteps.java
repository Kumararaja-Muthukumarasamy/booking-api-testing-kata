package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.DeleteBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.constants.SchemaPaths;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import com.booking.utils.IdConverter;
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

public class DeleteBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(DeleteBookingSteps.class);

    private Response response;
    private int bookingId;
    private BookingRequest originalBooking;
    private String token;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("the booking service for DeleteBooking is available")
    public void verify_delete_booking_service_is_available() {
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BASE_URL));
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT));
        logger.info("DeleteBooking service configuration verified");
    }

    @Given("a valid booking exists for delete")
    public void create_valid_booking_for_delete()  {
        originalBooking = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(originalBooking);
        response.then().statusCode(HTTPStatusCodes.CREATED);

        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
        assertThat(bookingId, greaterThan(0));

        logger.info("Created booking with ID {}", bookingId);
    }

    @Given("I am authenticated with valid token for delete")
    public void authenticate_with_valid_token_for_delete() {
        token = TokenManager.getToken();
        assertThat(token, not(emptyString()));
        assertThat(token, notNullValue());

        logger.info("Retrieved valid token for delete");
    }

    @Given("I use a {string} token for delete")
    public void configure_token_for_delete(String tokenType) {
        currentToken = TokenFactory.resolveToken(tokenType);
        logger.info("Configured {} token for delete", tokenType);
    }

    // ---------- WHEN ----------

    @When("I delete the booking")
    public void send_delete_booking_request() {
        response = DeleteBookingClient.deleteBooking(bookingId, token);
        logger.info("Sent DELETE request for booking ID {}", bookingId);
    }

    @When("I delete the booking using token")
    public void send_delete_booking_request_with_token() {
        response = DeleteBookingClient.deleteBooking(bookingId, currentToken);
        logger.info("Sent DELETE request for booking ID {} using configured token", bookingId);
    }

    @When("I delete the booking using id {string}")
    public void send_delete_booking_request_with_id(String idText) {
        int id = IdConverter.toIntOrDefault(idText, -1);
        response = DeleteBookingClient.deleteBooking(id, token);
        logger.info("Sent DELETE request with ID {}", id);
    }

    @When("I send delete request without id")
    public void send_delete_booking_request_without_id() {
        response = DeleteBookingClient.deleteBookingWithoutId(token);
        logger.info("Sent DELETE request without ID");
    }

    // ---------- THEN ----------

    @Then("the booking should be deleted successfully")
    public void verify_booking_deleted_successfully() {
        response.then().statusCode(HTTPStatusCodes.OK)
                .body(BookingResponseKeys.SUCCESS, equalTo(true));
        logger.info("Booking deleted successfully for ID {}", bookingId);

        // Add schema validation
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.DELETE_BOOKING_RESPONSE_SCHEMA);
        logger.debug("Validated delete response against {}", SchemaPaths.DELETE_BOOKING_RESPONSE_SCHEMA);
    }


    @Then("the delete request should fail with status {int} and error message {string}")
    public void verify_delete_failed_with_status_and_error(int statusCode, String errorMessage) {
        response.then().statusCode(statusCode)
                .body(BookingResponseKeys.ERROR, equalTo(errorMessage));
        logger.info("Delete failed with status {} and error '{}'", statusCode, errorMessage);
    }

    @Then("the delete request should fail with status {int}")
    public void verify_delete_failed_with_status(int statusCode) {
        response.then().statusCode(statusCode);
        logger.info("Delete failed with status {}", statusCode);
    }
}