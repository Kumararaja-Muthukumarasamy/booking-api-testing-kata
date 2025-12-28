package com.booking.stepdefinitions;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.utils.LoggerUtil;
import com.booking.utils.TokenFactory;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

public class CommonSteps {

    private static final Logger logger = LoggerUtil.getLogger(CommonSteps.class);
    private Response response;
    private String currentToken;

    // ---------- GIVEN ----------

    @Given("the booking service is available")
    public void verify_booking_service_is_available() {
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BASE_URL), "Base URL must be configured");
        Assertions.assertNotNull(ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT), "Booking endpoint must be configured");
        logger.info("Booking service configuration verified");
    }

    @Given("I use a {string} token")
    public void configure_token(String tokenType) {
        currentToken = TokenFactory.resolveToken(tokenType);
        logger.info("Configured {} token", tokenType);
    }


    // ---------- UTILITIES ----------
    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}