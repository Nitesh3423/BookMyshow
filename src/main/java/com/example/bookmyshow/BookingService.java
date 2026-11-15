package com.example.bookmyshow;

import java.time.Instant;
import java.util.List;

public class BookingService {
    public Booking startBooking(String bookingId, String userId, ShowSeat showSeat) {
        showSeat.hold();
        Instant holdExpiresAt = Instant.now().plusSeconds(300);
        return new Booking(
                bookingId,
                userId,
                showSeat.getShowId(),
                List.of(showSeat.getSeat().getId()),
                holdExpiresAt
        );
    }

    public void confirmPayment(Booking booking, ShowSeat showSeat) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not pending");
        }
        if (!Instant.now().isBefore(booking.getHoldExpiresAt())) {
            expireBooking(booking, showSeat);
            throw new IllegalStateException("Booking hold has expired");
        }

        if (showSeat.getStatus() != ShowSeatStatus.HELD) {
            throw new IllegalStateException("Seat is not held");
        }

        showSeat.confirmBooking();
        booking.confirm();
    }
    public void failPayment(Booking booking, ShowSeat showSeat) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not pending");
        }

        if (showSeat.getStatus() != ShowSeatStatus.HELD) {
            throw new IllegalStateException("Seat is not held");
        }

        showSeat.releaseHold();
        booking.markPaymentFailed();
    }
    public void expireBooking(Booking booking, ShowSeat showSeat) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not pending");
        }

        if (showSeat.getStatus() != ShowSeatStatus.HELD) {
            throw new IllegalStateException("Seat is not held");
        }

        if (Instant.now().isBefore(booking.getHoldExpiresAt())) {
            throw new IllegalStateException("Booking hold has not expired yet");
        }

        showSeat.releaseHold();
        booking.expire();
    }
}