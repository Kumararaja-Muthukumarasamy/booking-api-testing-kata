package com.booking.testdata;

import com.booking.model.BookingDates;
import com.booking.model.BookingRequest;
import com.booking.utils.DateUtil;
import net.datafaker.Faker;

import java.util.Random;

public class BookingDataFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private BookingDataFactory() {
    }

    public static BookingRequest validBooking() {

        BookingDates dates = DateUtil.validBookingDates();

        return new BookingRequest(
                generateRoomid(), generateFirstName(),
                generateLastName(), generateDepositPaid(),
                dates, generateEmail(), generatePhoneNumber()
        );
    }

    public static int generateRoomid() {
        return 100 + random.nextInt(900);
    }

    private static String generateFirstName() {
        String name = faker.name().firstName();
        return name.substring(0, Math.min(name.length(), 18));
    }

    private static String generateLastName() {
        String name = faker.name().firstName();
        return name.substring(0, Math.min(name.length(), 18));
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

    public static BookingRequest bookingWithoutRoomId() {
        return new BookingRequest(
                0, generateFirstName(),
                generateLastName(), generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutFirstname() {
        return new BookingRequest(
                generateRoomid(), null, generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutLastname() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(),
                null, generateDepositPaid(),
                DateUtil.validBookingDates(), generateEmail(), generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutCheckin() {
        BookingDates dates = new BookingDates(null, DateUtil.todayPlusDays(2));
        return new BookingRequest(
                generateRoomid(), generateFirstName(),
                generateLastName(), generateDepositPaid(),
                dates, generateEmail(), generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutCheckout() {
        BookingDates dates = new BookingDates(DateUtil.today(), null);
        return new BookingRequest(
                generateRoomid(), generateFirstName(),
                generateLastName(), generateDepositPaid(),
                dates, generateEmail(), generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutEmail() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                null, generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutPhone() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), null
        );
    }

    // --- Roomid ---
    public static BookingRequest roomidNegative() {
        return new BookingRequest(
                -1, generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    // --- Firstname ---
    public static BookingRequest firstnameTooShort() {
        return new BookingRequest(
                generateRoomid(), "Jo", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    public static BookingRequest firstnameTooLong() {
        return new BookingRequest(
                generateRoomid(), "Supercalifragilisticexpialidocious", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    // --- Lastname ---
    public static BookingRequest lastnameTooShort() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), "Go",
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    public static BookingRequest lastnameTooLong() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), "Supercalifragilisticexpialidocious",
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    // --- Dates ---
    public static BookingRequest invalidDateFormat() {
        BookingDates dates = new BookingDates("15-01-2026", "16-01-2026");

        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates,
                generateEmail(), generatePhoneNumber());
    }

    public static BookingRequest sameDate() {

        BookingDates dates = new BookingDates(DateUtil.today(), DateUtil.today());
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates,
                generateEmail(), generatePhoneNumber());
    }

    public static BookingRequest checkoutBeforeCheckin() {

        BookingDates dates = new BookingDates(DateUtil.today(), DateUtil.todayMinusDays(2));
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), dates,
                generateEmail(), generatePhoneNumber());
    }

    // --- Email---
    public static BookingRequest emailWithoutAt() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                "testgmail.com", generatePhoneNumber());
    }

    // --- Phone number---
    public static BookingRequest phoneNumberLengthLessThan_11() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), "12345");
    }

    public static BookingRequest phoneNumberLengthGreaterThan_21() {
        return new BookingRequest(
                generateRoomid(), generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), "12345567890123456789012345");
    }

       public static BookingRequest bookingWithInvalidField(String field) {
        return switch (field.toLowerCase()) {
            case "roomid_negative" -> roomidNegative();
            case "firstname_too_short" -> firstnameTooShort();
            case "firstname_too_long" -> firstnameTooLong();
            case "lastname_too_short" -> lastnameTooShort();
            case "lastname_too_long" -> lastnameTooLong();
            case "invalid_date_format" -> invalidDateFormat();
            case "same_checkin_checkout" -> sameDate();
            case "checkout_before_checkin" -> checkoutBeforeCheckin();
            case "email_without_at" -> emailWithoutAt();
            case "phone_lessthan_11" -> phoneNumberLengthLessThan_11();
            case "phone_greaterthan_21" -> phoneNumberLengthGreaterThan_21();

            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    public static BookingRequest firstnameLength3() {
        return new BookingRequest(
                generateRoomid(), "Raj", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }
    public static BookingRequest firstnameLength18() {
        return new BookingRequest(
                generateRoomid(), "RajiniRajiniRajini", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }

    public static BookingRequest lastnameLength3() {
        return new BookingRequest(
                generateRoomid(), "Raj", generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }
    public static BookingRequest lastnameLength30() {
        return new BookingRequest(
                generateRoomid(),generateFirstName(), "WellingtonWellingtonWellingt",
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), generatePhoneNumber());
    }
    public static BookingRequest phoneLength11() {
        return new BookingRequest(
                generateRoomid(),generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), "12345678901");
    }
    public static BookingRequest phoneLength21() {
        return new BookingRequest(
                generateRoomid(),generateFirstName(), generateLastName(),
                generateDepositPaid(), DateUtil.validBookingDates(),
                generateEmail(), "123456789011234567890");
    }
    public static BookingRequest bookingWithValidBoundary (String field){
            return switch (field.toLowerCase()) {
                case "firstname_length_3" -> firstnameLength3();
                case "firstname_length_18" -> firstnameLength18();
                case "lastname_length_3" -> lastnameLength3();
                case "lastname_length_30" -> lastnameLength30();
                case "phone_length_11" -> phoneLength11();
                case "phone_length_21" -> phoneLength21();

                default -> throw new IllegalArgumentException("Invalid field: " + field);
            };
        }
    public static BookingRequest bookingWithoutField(String field) {
        return switch (field.toLowerCase()) {
            case "firstname" -> new BookingRequest(
                    generateRoomid(), null, generateLastName(),
                    generateDepositPaid(),DateUtil.validBookingDates(),
                    generateEmail(), generatePhoneNumber()
            );
            case "checkin" -> {
                BookingDates dates = new BookingDates(null, DateUtil.todayPlusDays(2));
                yield new BookingRequest(
                        generateRoomid(), generateFirstName(), generateLastName(),
                        generateDepositPaid(), dates, generateEmail(), generatePhoneNumber()
                );
            }
            default -> throw new IllegalArgumentException("Unsupported missing field: " + field);
        };
    }
}