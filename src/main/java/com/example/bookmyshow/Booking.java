package com.example.bookmyshow;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.time.Instant;

public class Booking {
    private final String id;
    private final String userId;
    private final String showId;
    private final List<String> seatIds;
    private BookingStatus status;
    private final Instant holdExpiresAt;
    public Booking(String id, String userId, String showId, List<String> seatIds,Instant holdExpiresAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.showId = Objects.requireNonNull(showId);
        this.seatIds = List.copyOf(seatIds);
        this.holdExpiresAt = Objects.requireNonNull(holdExpiresAt);
        if (this.seatIds.isEmpty()) {
            throw new IllegalArgumentException("A booking must have at least one seat");
        }

        this.status = BookingStatus.PENDING;
    }
    public void confirm(){
        requiredPending();
        status=BookingStatus.CONFIRMED;
    }
    public void markPaymentFailed(){
        requiredPending();
        status=BookingStatus.PAYMENT_FAILED;
    }
    public void expire(){
        requiredPending();
        status= BookingStatus.EXPIRED;
    }
    private void requiredPending() {
        if(status!=BookingStatus.PENDING){
            throw new IllegalStateException("Only pending booking can change state");
        }
    }
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getShowId() {
        return showId;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public BookingStatus getStatus() {
        return status;
    }
    public Instant getHoldExpiresAt() {
        return holdExpiresAt;
    }
}
