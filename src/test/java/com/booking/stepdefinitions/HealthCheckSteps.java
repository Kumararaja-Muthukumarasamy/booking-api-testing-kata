package com.booking.stepdefinitions;

import com.booking.client.HealthCheckClient;
import com.booking.config.ConfigReader;
import com.booking.constants.APIPaths;
import com.booking.constants.HTTPStatusCodes;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;

public class HealthCheckSteps {
    private Response response;

    @Given("the booking service configuration is available")
    public void the_booking_service_configuration_is_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty("base.url"), "Base URL should be configured");

        Assertions.assertNotNull(
                APIPaths.HEALTH_CHECK, "Health check endpoint should be defined");
    }

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