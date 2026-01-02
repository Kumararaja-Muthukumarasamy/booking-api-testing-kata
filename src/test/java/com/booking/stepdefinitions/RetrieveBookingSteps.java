package com.booking.stepdefinitions;

import com.booking.client.RetrieveBookingClient;
import com.booking.constants.api.schema.SchemaPaths;
import com.booking.utils.context.ScenarioContext;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.validation.SchemaValidatorUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class RetrieveBookingSteps {

    private static final Logger logger =
            LoggerUtil.getLogger(RetrieveBookingSteps.class);

    private Response response;

    // -------------------------------------------------
    // WHEN
    // -------------------------------------------------

    @When("I retrieve the booking by ID")
    public void retrieve_booking_by_id() {

        Integer bookingId = (Integer) ScenarioContext.get("bookingId");
        String token = (String) ScenarioContext.get("token");

        Assertions.assertNotNull(
                bookingId,
                "BookingId must be present before GET booking"
        );

        logger.info(
                "Retrieving booking | bookingId={} | tokenPresent={}",
                bookingId,
                token != null
        );

        response = RetrieveBookingClient.getBookingById(bookingId, token);
        ScenarioContext.set("response", response);

        logger.info(
                "GET booking completed | status={}",
                response.getStatusCode()
        );
    }

    @When("I retrieve booking with ID {string}")
    public void retrieve_booking_with_id(String bookingIdText) {

        String token = (String) ScenarioContext.get("token");

        Object bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdText);
        } catch (NumberFormatException e) {
            bookingId = bookingIdText;
        }

        logger.info(
                "Retrieving booking with ID={} | tokenPresent={}",
                bookingId,
                token != null
        );

        response = RetrieveBookingClient.getBookingById(bookingId, token);
        ScenarioContext.set("response", response);

        logger.info(
                "GET booking executed | status={}",
                response.getStatusCode()
        );
    }

    // -------------------------------------------------
    // THEN
    // -------------------------------------------------

    @Then("the booking details should be returned successfully with status {int}")
    public void verify_get_booking_success(int expectedStatus) {

        int actualStatus = response.getStatusCode();

        logger.info(
                "Verifying GET booking status | expected={} | actual={}",
                expectedStatus,
                actualStatus
        );

        Assertions.assertEquals(
                expectedStatus,
                actualStatus,
                "Unexpected status code for GET booking"
        );
    }

    @Then("the get booking response should match the booking schema")
    public void verify_get_booking_schema() {

        SchemaValidatorUtil.validateSchema(
                response,
                SchemaPaths.BOOKING_RESPONSE_SCHEMA
        );

        logger.info("GET booking response matches booking schema");
    }
}