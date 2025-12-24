package com.booking.stepdefinitions;

import com.booking.config.ConfigReader;
import com.booking.constants.APIPaths;
import com.booking.constants.HTTPStatusCodes;
import com.booking.spec.RequestSpecFactory;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckSteps {
    private Response response;

    @When("I check the booking service health")
    public void i_check_the_booking_service_health() {
        response = given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .when()
                .get(ConfigReader.getBaseUrl()
                        + APIPaths.HEALTH_CHECK);
    }

    @Then("the booking service should be up and running")
    public void the_booking_service_should_be_up_and_running() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("status", equalTo("UP"));
    }
}
