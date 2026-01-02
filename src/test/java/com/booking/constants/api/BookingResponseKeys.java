package com.booking.constants.api;

public final class BookingResponseKeys {
    private BookingResponseKeys() {
    }

    // -------------------------------------------------
    // ROOT LEVEL KEYS
    // -------------------------------------------------

    public static final String BOOKING_ID = "bookingid";

    // -------------------------------------------------
    // BOOKING LEVEL KEYS
    // -------------------------------------------------

    public static final String ROOM_ID = "booking.roomid";
    public static final String FIRSTNAME = "booking.firstname";
    public static final String LASTNAME = "booking.lastname";
    public static final String DEPOSIT_PAID = "booking.depositpaid";
    public static final String CHECKIN = "booking.bookingdates.checkin";
    public static final String CHECKOUT = "booking.bookingdates.checkout";
    public static final String EMAIL = "booking.email";
    public static final String PHONE = "booking.phone";

    // -------------------------------------------------
    // ERROR KEYS
    // -------------------------------------------------

    public static final String ERROR = "error";
    public static final String ERRORS = "errors";
}