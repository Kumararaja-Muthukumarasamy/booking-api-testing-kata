package com.booking.stepdefinitions;

import com.booking.auth.TokenManager;
import com.booking.client.AuthClient;
import com.booking.config.ConfigReader;
import com.booking.constants.HTTPStatusCodes;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

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
                .body("token", notNullValue())
                .body("token", not(equalTo("")));

        String token = response.jsonPath().getString("token");
        TokenManager.setToken(token);
    }

    @When("I authenticate with username {string} and password {string}")
    public void i_authenticate_with_username_and_password(String username, String password) {
        response = AuthClient.generateToken(username, password);
    }

    @Then("authentication should fail with status {int} and error message {string}")
    public void authentication_should_fail_with_status_and_error_message(int status, String errorMessage) {
        response.then()
                .statusCode(status)
                .body("error", equalTo(errorMessage));
    }
}