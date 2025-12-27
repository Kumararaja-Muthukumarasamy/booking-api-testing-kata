package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.CreateBookingClient;
import com.booking.client.GetBookingClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.HTTPStatusCodes;
import com.booking.model.BookingRequest;
import com.booking.testdata.BookingDataFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetBookingByIDSteps {

    private Response response;
    private int bookingId;
    private BookingRequest bookingRequest;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("the booking service for GetBookingByID is available")
    public void service_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL));
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT));
    }

    @Given("a booking exists")
    public void booking_exists() {
        bookingRequest = BookingDataFactory.validBooking();
        response = CreateBookingClient.createBooking(bookingRequest);
        response.then().statusCode(HTTPStatusCodes.CREATED);
        bookingId = response.jsonPath().getInt("bookingid");
    }

    @Given("I am authenticated")
    public void authenticated() {
        currentToken = TokenManager.getToken();
        assertThat(currentToken, not(isEmptyOrNullString()));
    }

    @Given("I use a {string} token")
    public void use_token(String tokenType) {
        switch (tokenType) {
            case "missing" -> currentToken = null;
            case "empty" -> currentToken = "";
            case "invalid" -> currentToken = "invalid_token_123";
            default -> throw new IllegalArgumentException();
        }
    }

    // ---------- WHEN ----------

    @When("I retrieve the booking by ID")
    public void retrieve_booking() {
        response = GetBookingClient.getBookingByIdWithToken(bookingId, currentToken);
    }

    @When("I retrieve booking with ID {string}")
    public void retrieve_with_id(String bookingIdType) {
        String resolvedBookingId = switch (bookingIdType.toLowerCase()) {
            case "negative value" -> "-1";
            case "zero" -> "0";
            case "invalid" -> "999999";
            default -> throw new IllegalArgumentException(
                    "Unsupported booking ID type: " + bookingIdType);
        };
        response = GetBookingClient.getBookingByIdWithToken(
                resolvedBookingId, currentToken);
    }

    @When("I retrieve booking without ID")
    public void retrieve_without_id() {
        response = GetBookingClient.getBookingWithoutId(currentToken);
    }

    // ---------- THEN ----------

    @Then("the booking details should be returned successfully")
    public void booking_success() {
        response.then()
                .statusCode(200)
                .body("roomid", equalTo(bookingRequest.getRoomid()));
    }

    @Then("the request should fail with status {int} and error message {string}")
    public void request_failed(int status, String error) {
        response.then()
                .statusCode(status)
                .body("error", equalTo(error));
    }

    @Then("the request should fail with status {int}")
    public void request_failed_status(int status) {
        response.then().statusCode(status);
    }
}