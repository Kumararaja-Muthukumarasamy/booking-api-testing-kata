package com.booking.stepdefinitions;

import com.booking.client.DeleteBookingClient;
import com.booking.utils.auth.TokenManager;
import com.booking.utils.context.ScenarioContext;
import com.booking.utils.logging.LoggerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class DeleteBookingSteps {

    private static final Logger logger = LoggerUtil.getLogger(DeleteBookingSteps.class);

    private Response response;
    private String token;

    // -------------------------------------------------
    // GIVEN
    // -------------------------------------------------

    @Given("I am authenticated with a valid token")
    public void authenticate_with_valid_token() {

        token = TokenManager.getToken();

        Assertions.assertNotNull(token, "Token must not be null");
        Assertions.assertFalse(token.isEmpty(), "Token must not be empty");

        ScenarioContext.set("token", token);

        logger.info("Authenticated with valid token (state=PRESENT)");
    }

    // -------------------------------------------------
    // WHEN
    // -------------------------------------------------

    @When("I delete the booking")
    public void delete_booking() {

        Integer bookingId = (Integer) ScenarioContext.get("bookingId");
        String token = (String) ScenarioContext.get("token");

        Assertions.assertNotNull(
                bookingId,
                "BookingId must exist before delete operation"
        );

        logger.info(
                "Sending DELETE request | bookingId={} | tokenState={}",
                bookingId,
                token == null ? "MISSING" : "PRESENT"
        );

        response = DeleteBookingClient.deleteBooking(bookingId, token);
        ScenarioContext.set("response", response);
    }

    @When("I attempt to delete the booking")
    public void attempt_delete_booking() {

        Integer bookingId = (Integer) ScenarioContext.get("bookingId");
        String token = (String) ScenarioContext.get("token");

        Assertions.assertNotNull(
                bookingId,"BookingId must exist before attempting delete"
        );

        logger.info(
                "Attempting DELETE | bookingId={} | tokenState={}",
                bookingId,
                token == null ? "MISSING"
                        : token.isEmpty() ? "EMPTY" : "PRESENT"
        );

        response = DeleteBookingClient.deleteBooking(bookingId, token);
        ScenarioContext.set("response", response);
    }

    @When("I attempt to delete the booking using invalid id {string}")
    public void attempt_delete_with_invalid_id(String idText) {

        int invalidId = Integer.parseInt(idText);
        String token = (String) ScenarioContext.get("token");

        logger.info(
                "Attempting DELETE with invalid bookingId={} | tokenState={}",
                invalidId,
                token == null ? "MISSING" : "PRESENT"
        );

        response = DeleteBookingClient.deleteBooking(invalidId, token);
        ScenarioContext.set("response", response);
    }

    // -------------------------------------------------
    // THEN
    // -------------------------------------------------

    @Then("the booking should be deleted successfully with status {int}")
    public void verify_delete_success(int expectedStatus) {
        int actualStatus = response.getStatusCode();

        logger.info(
                "Delete successful | expectedStatus={} | actualStatus={}",
                expectedStatus, actualStatus
        );

        Assertions.assertEquals(
                expectedStatus, actualStatus, "Unexpected delete success status"
        );
    }
}