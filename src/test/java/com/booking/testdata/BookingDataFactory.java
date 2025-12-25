package com.booking.testdata;

import com.booking.model.BookingDates;
import com.booking.model.BookingRequest;

public class BookingDataFactory {
    private BookingDataFactory() {

    }

    public static BookingRequest validBooking() {

        BookingDates dates = new BookingDates(
                "2024-03-05",
                "2024-05-07"
        );

        return new BookingRequest(
                666,
                "XJohn",
                "XDoe",
                true,
                dates,
                "Xjohn.doe@example.com",
                "123456789012"
        );
    }
}
