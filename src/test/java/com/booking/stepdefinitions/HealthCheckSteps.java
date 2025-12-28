package com.booking.stepdefinitions;

import com.booking.client.HealthCheckClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.api.HTTPStatusCodes;
import com.booking.utils.logging.LoggerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;

public class HealthCheckSteps {

    private static final Logger logger = LoggerUtil.getLogger(HealthCheckSteps.class);
    private Response response;

    // ---------- GIVEN ----------

    @Given("the booking service configuration is available")
    public void verify_booking_service_configuration_is_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL), "Base URL should be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.HEALTH_ENDPOINT), "Health endpoint must be configured"
        );
        logger.info("Booking service configuration verified");
    }

    // ---------- WHEN ----------

    @When("I check the booking service health")
    public void send_health_check_request() {
        response = HealthCheckClient.getHealthStatus();
        logger.info("Sent health check request");
    }

    // ---------- THEN ----------

    @Then("the booking service should be up and running")
    public void verify_booking_service_is_up_and_running() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("status", equalTo("UP"));
        logger.info("Booking service health check passed with status {}", response.getStatusCode());
    }
}