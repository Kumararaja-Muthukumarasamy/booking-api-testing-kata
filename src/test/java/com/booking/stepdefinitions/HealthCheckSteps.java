package com.booking.stepdefinitions;

import com.booking.client.HealthCheckClient;
import com.booking.constants.HTTPStatusCodes;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class HealthCheckSteps {
    private Response response;

    @When("I check the booking service health")
    public void i_check_the_booking_service_health() {
        response = HealthCheckClient.getHealthStatus();
    }

    @Then("the booking service should be up and running")
    public void the_booking_service_should_be_up_and_running() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("status", equalTo("UP"));
    }
}