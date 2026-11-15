package com.example.bookmyshow;

import java.util.Objects;

public class Seat {
    private final String id;
    private final String screenId;
    private final String row;
    private final int number;

    public Seat(String id, String screenId, String row, int number) {
        this.id = Objects.requireNonNull(id);
        this.screenId = Objects.requireNonNull(screenId);
        this.row = Objects.requireNonNull(row);

        if (number <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }

        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getScreenId() {
        return screenId;
    }

    public String getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }
    public enum ShowSeatStaus{
        AVAILABLE,
        BOOKED,
        HELD
    }
}
