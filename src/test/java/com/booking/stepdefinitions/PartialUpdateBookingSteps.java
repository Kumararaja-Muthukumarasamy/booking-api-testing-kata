package com.booking.stepdefinitions;

import com.booking.client.PartialUpdateBookingClient;
import com.booking.constants.api.schema.SchemaPaths;
import com.booking.utils.context.ScenarioContext;
import com.booking.utils.logging.LoggerUtil;
import com.booking.utils.validation.SchemaValidatorUtil;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PartialUpdateBookingSteps {

    private static final Logger logger =
            LoggerUtil.getLogger(PartialUpdateBookingSteps.class);

    // -------------------------------------------------
    // WHEN
    // -------------------------------------------------

    @When("I patch the booking with partial details")
    public void patch_booking_with_partial_details() {

        Integer bookingId = (Integer) ScenarioContext.get("bookingId");
        String token = (String) ScenarioContext.get("token");

        Assertions.assertNotNull(
                bookingId,
                "BookingId must be available before PATCH operation"
        );

        Map<String, Object> patchRequest = new HashMap<>();
        patchRequest.put("firstname", "UpdatedName");
        patchRequest.put("depositpaid", true);

        logger.info(
                "Patching booking | bookingId={} | tokenState={}",
                bookingId,
                token == null
                        ? "MISSING"
                        : token.isEmpty()
                        ? "EMPTY"
                        : "PRESENT"
        );

        Response response =
                PartialUpdateBookingClient.patchBooking(
                        bookingId,
                        patchRequest,
                        token
                );

        ScenarioContext.set("response", response);

        logger.info(
                "PATCH booking executed | status={}",
                response.getStatusCode()
        );
    }

    @When("I patch the booking with ID {string} using partial details")
    public void patch_booking_with_invalid_id_using_partial_details(String bookingIdText) {

        String token = (String) ScenarioContext.get("token");
        int bookingId = Integer.parseInt(bookingIdText);

        Map<String, Object> patchRequest = new HashMap<>();
        patchRequest.put("firstname", "PatchedName");
        patchRequest.put("depositpaid", true);

        logger.info(
                "Patching booking with invalid ID | bookingId={} | tokenPresent={}",
                bookingId,
                token != null
        );

        Response response =
                PartialUpdateBookingClient.patchBooking(
                        bookingId,
                        patchRequest,
                        token
                );

        ScenarioContext.set("response", response);

        logger.info(
                "PATCH booking with invalid ID executed | status={}",
                response.getStatusCode()
        );
    }

    // -------------------------------------------------
    // THEN
    // -------------------------------------------------

    @Then("the booking should be partially updated successfully with status {int}")
    public void verify_patch_booking_success_status(int expectedStatus) {

        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before verifying PATCH success"
        );

        Assertions.assertEquals(
                expectedStatus,
                response.getStatusCode(),
                "Unexpected status code for PATCH booking"
        );
    }

    @Then("the patch booking response should match the booking schema")
    public void verify_patch_booking_response_schema() {

        Response response = (Response) ScenarioContext.get("response");

        Assertions.assertNotNull(
                response,
                "Response must be available before validating PATCH schema"
        );

        SchemaValidatorUtil.validateSchema(
                response,
                SchemaPaths.BOOKING_RESPONSE_SCHEMA
        );

        logger.info("PATCH booking response matches booking schema");
    }
}