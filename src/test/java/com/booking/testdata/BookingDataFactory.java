package com.booking.testdata;

import com.booking.model.BookingDates;
import com.booking.model.BookingRequest;
import com.github.javafaker.Faker;

import java.util.Random;

public class BookingDataFactory {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private BookingDataFactory() {
    }

    public static BookingRequest validBooking() {

        BookingDates dates = new BookingDates(
                "2026-01-05",
                "2026-01-07"
        );

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

    private static int generateRoomid() {
        return 100 + random.nextInt(900);
    }

    private static String generateFirstName() {
        return faker.lorem().characters(3, 18, true);
    }

    private static String generateLastName() {
        return faker.lorem().characters(3, 18, true);
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
}