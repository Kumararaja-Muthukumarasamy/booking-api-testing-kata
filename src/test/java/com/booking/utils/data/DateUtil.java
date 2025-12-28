package com.booking.utils;

import com.booking.model.BookingDates;

import java.time.LocalDate;

public final class DateUtil {

    private DateUtil() {

    }

    public static String today() {
        return LocalDate.now().toString();
    }

    public static String todayPlusDays(int days) {
        return LocalDate.now().plusDays(days).toString();
    }

    public static String todayMinusDays(int days) {
        return LocalDate.now().minusDays(days).toString();
    }

    public static BookingDates validBookingDates() {
        return new BookingDates(
                today(),
                todayPlusDays(2)
        );
    }
}