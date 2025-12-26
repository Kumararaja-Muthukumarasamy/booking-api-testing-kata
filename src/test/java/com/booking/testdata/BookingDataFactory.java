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
                generateRoomid(),
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                dates,
                generateEmail(),
                generatePhoneNumber()
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
                0,
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                DateUtil.validBookingDates(),
                generateEmail(),
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutFirstname() {
        return new BookingRequest(
                generateRoomid(),
                null,
                generateLastName(),
                generateDepositPaid(),
                DateUtil.validBookingDates(),
                generateEmail(),
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutLastname() {
        return new BookingRequest(
                generateRoomid(),
                generateFirstName(),
                null,
                generateDepositPaid(),
                DateUtil.validBookingDates(),
                generateEmail(),
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutCheckin() {
        BookingDates dates = new BookingDates(null, DateUtil.todayPlusDays(2));
        return new BookingRequest(
                generateRoomid(),
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                dates,
                generateEmail(),
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutCheckout() {
        BookingDates dates = new BookingDates(DateUtil.today(), null);
        return new BookingRequest(
                generateRoomid(),
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                dates,
                generateEmail(),
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutEmail() {
        return new BookingRequest(
                generateRoomid(),
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                DateUtil.validBookingDates(),
                null,
                generatePhoneNumber()
        );
    }

    public static BookingRequest bookingWithoutPhone() {
        return new BookingRequest(
                generateRoomid(),
                generateFirstName(),
                generateLastName(),
                generateDepositPaid(),
                DateUtil.validBookingDates(),
                generatePhoneNumber(),
                null
        );
    }
}