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

        BookingDates dates = new BookingDates(
                DateUtil.today(),
                DateUtil.todayPlusDays(2)
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
}