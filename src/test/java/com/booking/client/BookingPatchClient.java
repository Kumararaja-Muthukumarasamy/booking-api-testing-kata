package com.booking.client;

import com.booking.config.ConfigKey;
import com.booking.config.ConfigReader;
import com.booking.spec.RequestSpecFactory;
import com.booking.utils.logging.LoggerUtil;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class BookingPatchClient {

    private static final Logger logger = LoggerUtil.getLogger(BookingPatchClient.class);

    private BookingPatchClient() {
    }

    public static Response patchBooking(int bookingId, Object requestBody, String token) {
        logger.info("Patching booking with ID {}", bookingId);

        return given()
                .spec(RequestSpecFactory.getBaseRequestSpec())
                .headers(buildAuthHeader(token))
                .pathParam("id", bookingId)
                .body(requestBody)
                .when()
                .patch(buildEndpoint());
    }

    // ---------- helpers (low complexity) ----------

    private static String buildEndpoint() {
        return ConfigReader.getProperty(ConfigKey.BOOKING_ENDPOINT) + "/{id}";
    }

    private static java.util.Map<String, String> buildAuthHeader(String token) {
        if (token == null || token.isBlank()) {
            return java.util.Collections.emptyMap();
        }
        return java.util.Collections.singletonMap("Cookie", "token=" + token);
    }
}
