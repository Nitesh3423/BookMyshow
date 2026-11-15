package com.example.bookmyshow;

import java.math.BigDecimal;
import java.util.Objects;

public class ShowSeat {
    private final String showId;
    private final Seat seat;
    private final BigDecimal price;
    private ShowSeatStatus status;

    public ShowSeat(String showId, Seat seat, BigDecimal price) {
        this.showId = Objects.requireNonNull(showId);
        this.seat = Objects.requireNonNull(seat);
        this.price = Objects.requireNonNull(price);

        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.status = ShowSeatStatus.AVAILABLE;
    }

    public void hold() {
        if (status != ShowSeatStatus.AVAILABLE) {
            throw new IllegalStateException("Seat is not available");
        }
        status = ShowSeatStatus.HELD;
    }

    public void releaseHold() {
        if (status != ShowSeatStatus.HELD) {
            throw new IllegalStateException("Seat is not held");
        }
        status = ShowSeatStatus.AVAILABLE;
    }

    public void confirmBooking() {
        if (status != ShowSeatStatus.HELD) {
            throw new IllegalStateException("Only a held seat can be booked");
        }
        status = ShowSeatStatus.BOOKED;
    }

    public String getShowId() {
        return showId;
    }

    public Seat getSeat() {
        return seat;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ShowSeatStatus getStatus() {
        return status;
    }
}