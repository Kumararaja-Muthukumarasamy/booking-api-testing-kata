package com.booking.stepdefinitions;

import com.booking.client.AuthClient;
import com.booking.config.ConfigReader;
import com.booking.constants.HTTPStatusCodes;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.notNullValue;

public class AuthSteps {

    private Response response;

    @When("I authenticate with valid credentials")
    public void i_authenticate_with_valid_credentials() {
        response = AuthClient.generateToken(
                ConfigReader.getProperty("auth.username"),
                ConfigReader.getProperty("auth.password")
        );
    }

    @Then("an authentication token should be returned")
    public void an_authentication_token_should_be_returned() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("token", notNullValue());
    }
}