package com.booking.models.booking;

public class BookingRequest {
    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String email;
    private String phone;

    public BookingRequest(int roomid, String firstname,String lastname, boolean depositpaid,
                          BookingDates bookingdates, String email, String phone) {
        this.roomid = roomid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.email = email;
        this.phone = phone;
    }

    public int getRoomid() { return roomid; }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isDepositpaid() {
        return depositpaid;
    }

    public BookingDates getBookingdates() {
        return bookingdates;
    }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }
}