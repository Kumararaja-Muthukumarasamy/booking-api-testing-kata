package com.booking.stepdefinitions;

import com.booking.client.UpdateBookingClient;
import com.booking.constants.api.schema.SchemaPaths;
import com.booking.models.booking.BookingRequest;
import com.booking.testdata.booking.BookingTestDataFactory;
import com.booking.utils.context.ScenarioContext;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.validation.SchemaValidatorUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class UpdateBookingSteps {

    private static final Logger logger =
            LoggerUtil.getLogger(UpdateBookingSteps.class);

    // -------------------------------------------------
    // WHEN
    // -------------------------------------------------

    @When("I update the booking with valid details")
    public void update_booking_with_valid_details() {

        Integer bookingId = (Integer) ScenarioContext.get("bookingId");
        String token = (String) ScenarioContext.get("token");

        Assertions.assertNotNull(
                bookingId,
                "BookingId must be available before update"
        );

        BookingRequest updatedBooking =
                BookingTestDataFactory.updatedBooking();

        logger.info(
                "Updating booking | bookingId={} | tokenState={}",
                bookingId,
                token == null
                        ? "MISSING"
                        : token.isEmpty()
                        ? "EMPTY"
                        : "PRESENT"
        );

        Response updateResponse =
                UpdateBookingClient.updateBooking(
                        bookingId,
                        updatedBooking,
                        token
                );

        ScenarioContext.set("response", updateResponse);

        logger.info(
                "PUT booking executed | status={}",
                updateResponse.getStatusCode()
        );
    }

    @When("I update the booking with ID {string} using valid details")
    public void update_booking_with_invalid_id_using_valid_details(String bookingIdText) {

        int bookingId = Integer.parseInt(bookingIdText);
        String token = (String) ScenarioContext.get("token");

        BookingRequest updatedBooking =
                BookingTestDataFactory.updatedBooking();

        logger.info(
                "Updating booking with invalid ID | bookingId={} | tokenPresent={}",
                bookingId,
                token != null
        );

        Response updateResponse =
                UpdateBookingClient.updateBooking(
                        bookingId,
                        updatedBooking,
                        token
                );

        ScenarioContext.set("response", updateResponse);

        logger.info(
                "PUT booking with invalid ID executed | status={}",
                updateResponse.getStatusCode()
        );
    }

    // -------------------------------------------------
    // THEN
    // -------------------------------------------------

    @Then("the booking should be updated successfully with status {int}")
    public void verify_update_success_status(int expectedStatus) {

        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before verification"
        );

        int actualStatus = response.getStatusCode();

        logger.info(
                "Verifying update status | expected={} | actual={}",
                expectedStatus,
                actualStatus
        );

        Assertions.assertEquals(
                expectedStatus,
                actualStatus,
                "Unexpected status code for update booking"
        );
    }

    @Then("the update booking response should match the booking schema")
    public void verify_update_booking_response_schema() {

        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before schema validation"
        );

        SchemaValidatorUtil.validateSchema(
                response,
                SchemaPaths.BOOKING_RESPONSE_SCHEMA
        );

        logger.info("Update booking response matches booking schema");
    }
}