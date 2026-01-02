package com.booking.stepdefinitions;

import com.booking.client.CreateBookingClient;
import com.booking.client.DeleteBookingClient;
import com.booking.client.RetrieveBookingClient;
import com.booking.client.UpdateBookingClient;
import com.booking.constants.api.BookingResponseKeys;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.models.booking.BookingRequest;
import com.booking.testdata.booking.BookingTestDataFactory;
import com.booking.utils.auth.TokenManager;
import com.booking.utils.logging.LoggerUtil;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class BookingE2EFlowSteps {

    private static final Logger logger =
            LoggerUtil.getLogger(BookingE2EFlowSteps.class);

    private Integer bookingId;
    private String token;
    private BookingRequest createdBooking;
    private BookingRequest updatedBooking;

    /* =================================================
       FUNCTIONAL E2E FLOW (STATUS-BASED)
       ================================================= */

    @Given("a new booking is created")
    public void create_new_booking() {
        logger.info("===== FUNCTIONAL E2E FLOW STARTED =====");

        token = TokenManager.getToken();

        Assertions.assertNotNull(token, "Auth token must be available");

        Response response = CreateBookingClient.createBooking(BookingTestDataFactory.validBooking());

        Assertions.assertEquals(HTTPStatusCodes.CREATED, response.getStatusCode(), "Booking creation failed");

        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);

        Assertions.assertNotNull(bookingId, "BookingId must be generated");

        logger.info("Booking created successfully | bookingId={}", bookingId);
    }

    @When("the booking is retrieved by id and verified successfully")
    public void retrieve_booking_and_verify() {

        Response response = RetrieveBookingClient.getBookingById(bookingId, token);

        Assertions.assertEquals(HTTPStatusCodes.OK, response.getStatusCode(), "Failed to retrieve booking");

        logger.info("Booking retrieved successfully");
    }

    @When("the booking is updated with new data")
    public void update_booking() {
        Response response = UpdateBookingClient.updateBooking(
                bookingId,
                BookingTestDataFactory.updatedBooking(),
                token
        );

        Assertions.assertEquals(
                HTTPStatusCodes.OK, response.getStatusCode(), "Booking update failed"
        );

        logger.info("Booking updated successfully");
    }

    @When("the booking is retrieved again and confirmed that updates are reflected")
    public void retrieve_booking_after_update() {
        Response response = RetrieveBookingClient.getBookingById(bookingId, token);

        Assertions.assertEquals(
                HTTPStatusCodes.OK,
                response.getStatusCode(), "Failed to retrieve booking after update"
        );

        logger.info("Updated booking retrieved successfully");
    }

    @Then("the booking is deleted successfully")
    public void delete_booking() {
        Response response = DeleteBookingClient.deleteBooking(bookingId, token);

        Assertions.assertTrue(
                response.getStatusCode() == HTTPStatusCodes.OK, "Booking deletion failed");

        logger.info("Booking deleted successfully");
        logger.info("===== FUNCTIONAL E2E FLOW COMPLETED =====");
    }

    /* =================================================
       DATA-CONTRACT E2E FLOW (INTENTIONAL FAILURE)
       ================================================= */

    @Given("a booking is created as per data contract")
    public void create_booking_data_contract() {
        logger.info("===== DATA-CONTRACT E2E FLOW STARTED =====");

        token = TokenManager.getToken();
        Assertions.assertNotNull(token, "Auth token must be available");

        createdBooking = BookingTestDataFactory.validBooking();

        Response response =
                CreateBookingClient.createBooking(createdBooking);

        Assertions.assertEquals(
                HTTPStatusCodes.OK,
                response.getStatusCode(), "Data contract violated: CREATE should return 200 OK"
        );

        bookingId = response.jsonPath().getInt(BookingResponseKeys.BOOKING_ID);
    }

    @When("the booking is retrieved by id as per data contract")
    public void retrieve_booking_data_contract() {
        Response response = RetrieveBookingClient.getBookingById(bookingId, token);

        Assertions.assertEquals(
                HTTPStatusCodes.OK, response.getStatusCode(), "GET booking failed"
        );

        Assertions.assertEquals(
                createdBooking.getFirstname(), response.jsonPath().getString("firstname"),
                "Firstname mismatch after CREATE"
        );

        Assertions.assertEquals(
                createdBooking.getLastname(), response.jsonPath().getString("lastname"),
                "Lastname mismatch after CREATE"
        );
    }

    @When("the booking is updated as per data contract")
    public void update_booking_data_contract() {
        updatedBooking = BookingTestDataFactory.updatedBooking();
        Response response = UpdateBookingClient.updateBooking(
                bookingId,
                updatedBooking,
                token
        );

        Assertions.assertEquals(
                HTTPStatusCodes.OK, response.getStatusCode(), "Update booking failed"
        );

        Assertions.assertEquals(
                updatedBooking.getFirstname(), response.jsonPath().getString("firstname"),
                "Firstname mismatch after UPDATE"
        );
    }

    @When("the booking is retrieved again as per data contract")
    public void retrieve_booking_again_data_contract() {
        Response response = RetrieveBookingClient.getBookingById(bookingId, token);

        Assertions.assertEquals(
                HTTPStatusCodes.OK, response.getStatusCode(), "GET booking after update failed"
        );

        Assertions.assertEquals(
                updatedBooking.getFirstname(), response.jsonPath().getString("firstname"), "Firstname mismatch after UPDATE"
        );
    }
}