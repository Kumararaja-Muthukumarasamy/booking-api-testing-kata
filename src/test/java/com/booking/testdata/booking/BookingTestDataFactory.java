package com.booking.testdata.booking;

import com.booking.models.booking.BookingDates;
import com.booking.models.booking.BookingRequest;
import com.booking.utils.data.DateUtil;
import net.datafaker.Faker;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BookingTestDataFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private BookingTestDataFactory() {
    }

    // ---------------- VALID BOOKING ----------------
    public static BookingRequest validBooking() {
        BookingDates dates = DateUtil.validBookingDates();
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
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

    //---------------- Factory switch – missing mandatory fields dispatcher
    public static Object bookingWithMissingField(String field) {
        return switch (field.toLowerCase()) {
            case "roomid" -> bookingWithoutRoomId();
            case "firstname" -> bookingWithoutFirstname();
            case "lastname" -> bookingWithoutLastname();
            case "depositpaid" -> bookingWithoutDepositPaid();
            case "bookingdates" -> bookingWithoutBookingDates();
            case "email" -> bookingWithoutEmail();
            case "phone" -> bookingWithoutPhone();
            default -> throw new IllegalArgumentException("Invalid mandatory field: " + field);
        };
    }

    //---missing field generators---

    public static Map<String, Object> bookingWithoutRoomId() {

        Map<String, Object> request = new HashMap<>();

        request.put("firstname", generateFirstName());
        request.put("lastname", generateLastName());

        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", DateUtil.today());
        bookingDates.put("checkout", DateUtil.todayPlusDays(2));

        request.put("bookingdates", bookingDates);
        request.put("depositpaid", generateDepositPaid());
        request.put("email", generateEmail());
        request.put("phone", generatePhoneNumber());

        return request;
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

    public static Map<String, Object> bookingWithoutDepositPaid() {

        Map<String, Object> request = new HashMap<>();

        request.put("roomid", generateRoomid());
        request.put("firstname", generateFirstName());
        request.put("lastname", generateLastName());

        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", DateUtil.today());
        bookingDates.put("checkout", DateUtil.todayPlusDays(2));

        request.put("bookingdates", bookingDates);
        request.put("email", generateEmail());
        request.put("phone", generatePhoneNumber());

        return request;
    }

    private static BookingRequest bookingWithoutBookingDates() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), null, generateEmail(), generatePhoneNumber());
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

    //--------------------RoomId invalid values----------------
    private static BookingRequest bookingWithNegativeRoomId() {
        return new BookingRequest(-1, generateFirstName(), generateLastName(), generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithZeroRoomId() {
        return new BookingRequest(0, generateFirstName(), generateLastName(), generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    //--------------------Invalid Roomid methods----------------
    private static Map<String, Object> bookingWithStringRoomId() {
        Map<String, Object> request = baseBookingMap();
        request.put("roomid", "ABC");
        return request;
    }

    private static Map<String, Object> bookingWithBooleanRoomId() {
        Map<String, Object> request = baseBookingMap();
        request.put("roomid", true);
        return request;
    }

    private static Map<String, Object> bookingWithDecimalRoomId() {
        Map<String, Object> request = baseBookingMap();
        request.put("roomid", 12.34);
        return request;
    }

    //--------------------Base map helper method----------------
    private static Map<String, Object> baseBookingMap() {
        Map<String, Object> request = new HashMap<>();
        request.put("firstname", generateFirstName());
        request.put("lastname", generateLastName());
        request.put("depositpaid", generateDepositPaid());

        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", DateUtil.today());
        bookingDates.put("checkout", DateUtil.todayPlusDays(2));

        request.put("bookingdates", bookingDates);
        request.put("email", generateEmail());
        request.put("phone", generatePhoneNumber());

        return request;
    }

    //--------------------Invalid Firstname methods
    private static BookingRequest bookingWithFirstnameTooShort() {
        return new BookingRequest(generateRoomid(), "Jo", generateLastName(), generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithFirstnameTooLong() {
        return new BookingRequest(generateRoomid(), "ABCDEFGHIJKLMNOPQRST", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithFirstnameBlank() {
        return new BookingRequest(generateRoomid(), "", generateLastName(), generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    //--------------------Invalid Lastname methods
    private static BookingRequest bookingWithLastnameTooShort() {
        return new BookingRequest(generateRoomid(), generateFirstName(), "Do",
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithLastnameTooLong() {
        return new BookingRequest(generateRoomid(), generateFirstName(), "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE",
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithLastnameBlank() {
        return new BookingRequest(generateRoomid(), generateFirstName(),
                "", generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    //--------------------Invalid Phone methods
    private static BookingRequest bookingWithPhoneLessThan11() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), "123456789"
        );
    }

    private static BookingRequest bookingWithPhoneGreaterThan21() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), "1234567890123456789012345"
        );
    }

    //--------------------Invalid Email methods
    private static BookingRequest bookingWithEmailMissingAt() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), "invalid.email.com", generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithEmailMissingDomain() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), "user@", generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithEmailMissingUsername() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), "@domain.com", generatePhoneNumber()
        );
    }

    //-----------------------Invalid Booking Date Methods
    private static BookingRequest bookingWithCheckoutBeforeCheckin() {
        BookingDates dates = new BookingDates(
                DateUtil.todayPlusDays(2),
                DateUtil.today()
        );
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithSameCheckinCheckout() {
        BookingDates dates = new BookingDates(
                DateUtil.today(),
                DateUtil.today()
        );
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
        );
    }

    private static Map<String, Object> bookingWithInvalidDateFormat() {

        Map<String, Object> booking = new HashMap<>();
        booking.put("roomid", generateRoomid());
        booking.put("firstname", generateFirstName());
        booking.put("lastname", generateLastName());
        booking.put("depositpaid", generateDepositPaid());

        Map<String, Object> dates = new HashMap<>();
        dates.put("checkin", "12/30/2025");   // invalid format
        dates.put("checkout", "01/01/2026");

        booking.put("bookingdates", dates);
        booking.put("email", generateEmail());
        booking.put("phone", generatePhoneNumber());

        return booking;
    }

    //----------------Factory switch – invalid fields dispatcher
    public static Object bookingWithInvalidField(String field) {
        return switch (field.toLowerCase()) {
            // ---------- RoomId ----------
            case "roomid_negative" -> bookingWithNegativeRoomId();
            case "roomid_zero" -> bookingWithZeroRoomId();
            case "roomid_string" -> bookingWithStringRoomId();
            case "roomid_boolean" -> bookingWithBooleanRoomId();
            case "roomid_decimal" -> bookingWithDecimalRoomId();

            // ---------- Firstname ----------
            case "firstname_too_short" -> bookingWithFirstnameTooShort();
            case "firstname_too_long" -> bookingWithFirstnameTooLong();
            case "firstname_blank" -> bookingWithFirstnameBlank();

            // ---------- Lastname ----------
            case "lastname_too_short" -> bookingWithLastnameTooShort();
            case "lastname_too_long" -> bookingWithLastnameTooLong();
            case "lastname_blank" -> bookingWithLastnameBlank();

            //------------ Booking Dates-------------------------
            case "checkout_before_checkin" -> bookingWithCheckoutBeforeCheckin();
            case "same_day_booking" -> bookingWithSameCheckinCheckout();
            case "invalid_date_format" -> bookingWithInvalidDateFormat();

            // ---------- Phone ----------
            case "phone_lessthan_11" -> bookingWithPhoneLessThan11();
            case "phone_greaterthan_21" -> bookingWithPhoneGreaterThan21();

            // ---------- Email----------
            case "email_missing_at" -> bookingWithEmailMissingAt();
            case "email_missing_domain" -> bookingWithEmailMissingDomain();
            case "email_missing_username" -> bookingWithEmailMissingUsername();

            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    //--------------------Valid Voundary - Firstname method

    private static BookingRequest bookingWithValidFirstnameMin() {
        return new BookingRequest(generateRoomid(), "ABC", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithValidFirstnameMax() {
        return new BookingRequest(generateRoomid(), "ABCDEFGHIJKLMNOPQR", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    //--------------------Valid Boundary - Lastname method
    private static BookingRequest bookingWithValidLastnameMin() {
        return new BookingRequest(generateRoomid(), generateFirstName(), "ABC",
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    private static BookingRequest bookingWithValidLastnameMax() {
        return new BookingRequest(generateRoomid(), generateFirstName(), "ABCDEFGHIJKLMNOPQR",
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    //--------------------Valid Boundary - Phone method
    private static BookingRequest bookingWithValidPhoneMin() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), "12345678901"
        );
    }

    private static BookingRequest bookingWithValidPhoneMax() {
        return new BookingRequest(generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(), generateEmail(), "123456789012345678901"
        );
    }

    //--------------------Factory switch – valid boundary fields dispatcher
    public static BookingRequest bookingWithValidBoundary(String field) {
        return switch (field.toLowerCase()) {
            case "firstname_length_3" -> bookingWithValidFirstnameMin();
            case "firstname_length_18" -> bookingWithValidFirstnameMax();
            case "lastname_length_3" -> bookingWithValidLastnameMin();
            case "lastname_length_18" -> bookingWithValidLastnameMax();
            case "phone_length_11" -> bookingWithValidPhoneMin();
            case "phone_length_21" -> bookingWithValidPhoneMax();
            default -> throw new IllegalArgumentException("Invalid field: " + field);
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
}