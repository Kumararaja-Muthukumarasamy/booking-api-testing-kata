package com.booking.utils.data;

public class IdConverter {

    private IdConverter() {
        // prevent instantiation
    }

    public static int toIntOrDefault(String idText, int defaultValue) {
        try {
            return Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}