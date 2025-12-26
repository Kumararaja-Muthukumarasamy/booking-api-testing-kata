package com.booking.utils;

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
}
