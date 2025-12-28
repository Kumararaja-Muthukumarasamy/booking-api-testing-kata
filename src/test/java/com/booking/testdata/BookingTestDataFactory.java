package com.booking.testdata;

import com.booking.model.BookingDates;
import com.booking.model.BookingRequest;
import com.booking.utils.DateUtil;
import com.booking.utils.LoggerUtil;
import net.datafaker.Faker;

import java.util.Map;
import java.util.Random;

public class BookingDataFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private BookingDataFactory() {
    }

    // ---------------- VALID BOOKING ----------------
    public static BookingRequest validBooking() {
        BookingDates dates = DateUtil.validBookingDates();
        return new BookingRequest(
                generateRoomid(), generateFirstName(),
                generateLastName(), generateDepositPaid(),
                dates, generateEmail(), generatePhoneNumber()
        );
    }

    // ---------------- GENERATORS ----------------
    public static int generateRoomid() {
        return 100 + random.nextInt(900);
    }

    private static String generateFirstName() {
        String name = faker.name().firstName();
        return name.substring(0, Math.min(name.length(), 18));
    }

    private static String generateLastName() {
        String name = faker.name().lastName();
        return name.substring(0, Math.min(name.length(), 30));
    }

    private static boolean generateDepositPaid() {
        return faker.bool().bool();
    }

    private static String generateEmail() {
        return faker.internet().emailAddress();
    }

    private static String generatePhoneNumber() {
        return faker.number().digits(11);
    }

    // ---------------- NEGATIVE BOOKING ----------------
    public static BookingRequest bookingWithMissingField(String field) {
        return switch (field.toLowerCase()) {
            case "roomid" -> bookingWithoutRoomId();
            case "firstname" -> bookingWithoutFirstname();
            case "lastname" -> bookingWithoutLastname();
            case "checkin" -> bookingWithoutCheckin();
            case "checkout" -> bookingWithoutCheckout();
            case "email" -> bookingWithoutEmail();
            case "phone" -> bookingWithoutPhone();
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    private static BookingRequest bookingWithoutRoomId() {
        return new BookingRequest(0, generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutFirstname() {
        return new BookingRequest(generateRoomid(), null, generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutLastname() {
        return new BookingRequest(generateRoomid(), generateFirstName(), null,
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutCheckin() {
        BookingDates dates = new BookingDates(null, DateUtil.todayPlusDays(2));
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates, generateEmail(), generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutCheckout() {
        BookingDates dates = new BookingDates(DateUtil.today(), null);
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates, generateEmail(), generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutEmail() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                null, generatePhoneNumber());
    }

    private static BookingRequest bookingWithoutPhone() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), null);
    }

    // ---------------- INVALID BOOKING ----------------
    public static BookingRequest bookingWithInvalidField(String field) {
        return switch (field.toLowerCase()) {
            case "roomid_negative" -> new BookingRequest(-1, generateFirstName(), generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "firstname_too_short" -> new BookingRequest(generateRoomid(), "Jo", generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "firstname_too_long" -> new BookingRequest(generateRoomid(),
                    "Supercalifragilisticexpialidocious", generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "lastname_too_short" -> new BookingRequest(generateRoomid(), generateFirstName(), "Go",
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "lastname_too_long" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    "Supercalifragilisticexpialidocious",
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "invalid_date_format" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    new BookingDates("15-01-2026", "16-01-2026"),
                    generateEmail(), generatePhoneNumber());
            case "same_checkin_checkout" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    new BookingDates(DateUtil.today(), DateUtil.today()),
                    generateEmail(), generatePhoneNumber());
            case "checkout_before_checkin" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    new BookingDates(DateUtil.today(), DateUtil.todayMinusDays(2)),
                    generateEmail(), generatePhoneNumber());
            case "email_without_at" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), "testgmail.com", generatePhoneNumber());
            case "phone_lessthan_11" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), "12345");
            case "phone_greaterthan_21" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), "12345567890123456789012345");
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }
    public static BookingRequest bookingWithoutField(String field) {
        return switch (field.toLowerCase()) {
            case "roomid" -> new BookingRequest(
                    0, generateFirstName(), generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber()
            );
            case "firstname" -> new BookingRequest(
                    generateRoomid(), null, generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber()
            );
            case "lastname" -> new BookingRequest(
                    generateRoomid(), generateFirstName(), null,
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber()
            );
            case "checkin" -> {
                BookingDates dates = new BookingDates(null, DateUtil.todayPlusDays(2));
                yield new BookingRequest(
                        generateRoomid(), generateFirstName(), generateLastName(),
                        generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
                );
            }
            case "checkout" -> {
                BookingDates dates = new BookingDates(DateUtil.today(), null);
                yield new BookingRequest(
                        generateRoomid(), generateFirstName(), generateLastName(),
                        generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
                );
            }
            case "email" -> new BookingRequest(
                    generateRoomid(), generateFirstName(), generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    null, generatePhoneNumber()
            );
            case "phone" -> new BookingRequest(
                    generateRoomid(), generateFirstName(), generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), null
            );
            default -> throw new IllegalArgumentException("Unsupported missing field: " + field);
        };
    }
    public static BookingRequest updatedBooking() {
        BookingDates dates = DateUtil.validBookingDates();
        return new BookingRequest(
                generateRoomid(),
                "UpdatedFirstName",
                "UpdatedLastName",
                true,
                dates,
                "updated.email@example.com",
                "98765432101"
        );
    }

    // ---------------- VALID BOUNDARY BOOKING ----------------
    public static BookingRequest bookingWithValidBoundary(String field) {
        return switch (field.toLowerCase()) {
            case "firstname_length_3" -> new BookingRequest(generateRoomid(), "Raj", generateLastName(),
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "firstname_length_18" -> new BookingRequest(generateRoomid(), "RajiniRajiniRajini",
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber());
            case "lastname_length_3" -> new BookingRequest(generateRoomid(), generateFirstName(), "Doe",
                    generateDepositPaid(), DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber());
            case "lastname_length_30" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    "WellingtonWellingtonWelli", generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber());
            case "phone_length_11" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), "12345678901");
            case "phone_length_21" -> new BookingRequest(generateRoomid(), generateFirstName(),
                    generateLastName(), generateDepositPaid(),
                    DateUtil.validBookingDates(), generateEmail(), "123456789011234567890");
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    // ---------------- PATCH DATA ----------------

    public static Map<String, Object> getValidPatchData(String field) {
        return switch (field.toLowerCase()) {
            case "firstname" -> Map.of("firstname", "Raja");
            case "lastname" -> Map.of("lastname", "Dinesh");
            case "depositpaid" -> Map.of("depositpaid", true);
            case "roomid" -> Map.of("roomid", 348);
            default -> throw new IllegalArgumentException("Unsupported valid patch field: " + field);
        };
    }

    public static Map<String, Object> getInvalidPatchData(String field) {
        return switch (field.toLowerCase()) {
            case "firstname_too_short" -> Map.of("firstname", "Ra");
            case "firstname_too_long" -> Map.of("firstname", "RajaAutomationFrameworkDesign");
            case "lastname_too_short" -> Map.of("lastname", "Li");
            case "lastname_too_long" -> Map.of("lastname", "DineshAutomationFrameworkBoundaryCheck");
            case "invalid_date_format" -> Map.of(
                    "bookingdates", Map.of("checkin", "2025/12/27", "checkout", "2025/12/29")
            );
            case "same_checkin_checkout" -> Map.of(
                    "bookingdates", Map.of("checkin", "2025-12-27", "checkout", "2025-12-27")
            );
            case "checkout_before_checkin" -> Map.of(
                    "bookingdates", Map.of("checkin", "2025-12-29", "checkout", "2025-12-27")
            );
            case "email_without_at" -> Map.of("email", "invalidemail.com");
            case "phone_lessthan_11" -> Map.of("phone", "123456789");
            case "phone_greaterthan_21" -> Map.of("phone", "12345678901234567890123");
            default -> throw new IllegalArgumentException("Unsupported invalid patch field: " + field);
        };
    }
    public static String resolveBookingId(String bookingIdType) {
        return switch (bookingIdType.toLowerCase()) {
            case "negative value" -> "-1";
            case "zero" -> "0";
            case "invalid" -> "999999";
            default -> throw new IllegalArgumentException("Unsupported booking ID type: " + bookingIdType);
        };
    }
    // ---------------- SAFE WRAPPERS ----------------
    public static BookingRequest safeBookingWithMissingField(String field) {
        try {
            return bookingWithMissingField(field);
        } catch (IllegalArgumentException e) {
            LoggerUtil.getLogger(BookingDataFactory.class)
                    .error("Unsupported missing field: {}", field, e);
            return validBooking(); // fallback to a valid booking
        }
    }

    public static BookingRequest safeBookingWithInvalidField(String field) {
        try {
            return bookingWithInvalidField(field);
        } catch (IllegalArgumentException e) {
            LoggerUtil.getLogger(BookingDataFactory.class)
                    .error("Unsupported invalid field: {}", field, e);
            return validBooking(); // fallback to a valid booking
        }
    }

    public static BookingRequest safeBookingWithValidBoundary(String field) {
        try {
            return bookingWithValidBoundary(field);
        } catch (IllegalArgumentException e) {
            LoggerUtil.getLogger(BookingDataFactory.class)
                    .error("Unsupported valid boundary field: {}", field, e);
            return validBooking(); // fallback to a valid booking
        }
    }

}