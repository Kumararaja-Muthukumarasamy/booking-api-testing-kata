package com.booking.stepdefinitions;

import com.booking.client.AuthClient;
import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.constants.BookingResponseKeys;
import com.booking.constants.HTTPStatusCodes;
import com.booking.constants.SchemaPaths;
import com.booking.model.AuthRequest;
import com.booking.utils.LoggerUtil;
import com.booking.utils.SchemaValidatorUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.*;

public class AuthSteps {

    private static final Logger logger = LoggerUtil.getLogger(AuthSteps.class);

    private Response response;

    // ---------- GIVEN ----------//

    @Given("the authentication service is available")
    public void authentication_service_available() {
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.BASE_URL), "Base URL must be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.AUTH_ENDPOINT), "Auth endpoint must be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.AUTH_USERNAME), "Auth username must be configured"
        );
        Assertions.assertNotNull(
                ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD), "Auth password must be configured"
        );

        logger.info("Authentication service configuration verified");
    }

    // ---------- WHEN ----------//

    @When("I authenticate with valid credentials")
    public void authenticate_with_valid_credentials() {
        response = AuthClient.generateToken(
                ConfigReader.getProperty(ConfigKey.AUTH_USERNAME),
                ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD)
        );
        logger.info("Sent authentication request with valid credentials");
    }

    @When("I authenticate with username {string} and password {string}")
    public void authenticate_with_username_and_password(String username, String password) {
        response = AuthClient.generateToken(username, password);
        logger.info("Sent authentication request with provided credentials");
    }

    // ---------- THEN ----------//

    @Then("a valid authentication token should be returned")
    public void authentication_token_returned_successfully() {
        response.then()
                .statusCode(HTTPStatusCodes.OK)
                .body("token", notNullValue())
                .body("token", not(equalTo("")));
        logger.info("Authentication succeeded, token returned");
    }

    @Then("authentication should fail with status {int} and error {string}")
    public void authentication_failed_with_status_and_error(int status, String errorMessage) {
        response.then()
                .statusCode(status)
                .body(BookingResponseKeys.ERROR, equalTo(errorMessage));
        logger.info("Authentication failed with status {} and error '{}'", status, errorMessage);
    }

    @Then("the authentication request should match the auth-request schema")
    public void validate_authentication_request_schema() {
        AuthRequest authRequest = new AuthRequest(
                ConfigReader.getProperty(ConfigKey.AUTH_USERNAME),
                ConfigReader.getProperty(ConfigKey.AUTH_PASSWORD)
        );
        SchemaValidatorUtil.validateSchema(authRequest, SchemaPaths.AUTH_REQUEST_SCHEMA);
        logger.debug("Validated authentication request against {}", SchemaPaths.AUTH_REQUEST_SCHEMA);
    }

    @Then("the authentication response should match the auth-response schema")
    public void validate_authentication_response_schema() {
        SchemaValidatorUtil.validateSchema(response, SchemaPaths.AUTH_RESPONSE_SCHEMA);
        logger.debug("Validated authentication response against {}", SchemaPaths.AUTH_RESPONSE_SCHEMA);
    }
}