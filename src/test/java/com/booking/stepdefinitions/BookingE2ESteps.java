package com.booking.stepdefinitions;

import com.booking.client.*;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.testdata.BookingTestDataFactory;
import com.booking.utils.logging.LoggerUtil;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingE2ESteps {

    private static final Logger logger =
            LoggerUtil.getLogger(BookingE2ESteps.class);

    private int bookingId;
    private String token;

    @Given("a new booking is created")
    public void createBooking() {
        logger.info("===== E2E FLOW STARTED =====");

        logger.info("Generating auth token");
        token = AuthClient.generateToken("admin", "password")
                .jsonPath()
                .getString("token");

        logger.info("Auth token generated successfully");

        logger.info("Creating new booking");
        Response response = CreateBookingClient.createBooking(
                BookingTestDataFactory.validBooking()
        );

        logger.info("Create Booking Status Code: {}", response.getStatusCode());
        logger.debug("Create Booking Response: {}", response.asString());

        assertEquals(HTTPStatusCodes.CREATED, response.getStatusCode());

        bookingId = response.jsonPath().getInt("bookingid");
        assertNotNull(bookingId);

        logger.info("Booking created successfully with ID: {}", bookingId);
    }

    @When("the booking is retrieved by id")
    public void getBookingById() {
        logger.info("Retrieving booking with ID: {}", bookingId);

        Response response = GetBookingClient.getBookingById(bookingId, token);

        logger.info("Get Booking Status Code: {}", response.getStatusCode());
        logger.debug("Get Booking Response: {}", response.asString());

        assertEquals(HTTPStatusCodes.OK, response.getStatusCode());
    }

    @When("the booking is updated")
    public void updateBooking() {
        logger.info("Updating booking with ID: {}", bookingId);

        Response response = BookingPutClient.updateBooking(
                bookingId,
                BookingTestDataFactory.updatedBooking(),
                token
        );

        logger.info("Update Booking Status Code: {}", response.getStatusCode());
        logger.debug("Update Booking Response: {}", response.asString());

        assertEquals(HTTPStatusCodes.OK, response.getStatusCode());
    }

    @When("the booking is retrieved again")
    public void getBookingAgain() {
        logger.info("Retrieving booking again with ID: {}", bookingId);

        Response response = GetBookingClient.getBookingById(bookingId, token);

        logger.info("Get Booking (After Update) Status Code: {}", response.getStatusCode());
        logger.debug("Get Booking (After Update) Response: {}", response.asString());

        assertEquals(HTTPStatusCodes.OK, response.getStatusCode());
    }

    @Then("the booking is deleted successfully")
    public void deleteBooking() {
        logger.info("Deleting booking with ID: {}", bookingId);

        Response response = DeleteBookingClient.deleteBooking(bookingId, token);

        logger.info("Delete Booking Status Code: {}", response.getStatusCode());
        logger.debug("Delete Booking Response: {}", response.asString());

        assertEquals(HTTPStatusCodes.OK, response.getStatusCode());

        logger.info("Booking deleted successfully with ID: {}", bookingId);
        logger.info("===== E2E FLOW COMPLETED =====");
    }
}