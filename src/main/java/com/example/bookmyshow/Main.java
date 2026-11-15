package com.example.bookmyshow;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Seat seat = new Seat("A-5", "screen-1", "A", 5);
        ShowSeat showSeat = new ShowSeat("show-6pm", seat, new BigDecimal("12.50"));
        BookingService bookingService = new BookingService();
        Booking booking= bookingService.startBooking("booking-1","user-1",showSeat);

        System.out.println("Before payment: " + booking.getStatus());

        bookingService.confirmPayment(booking, showSeat);

        System.out.println("After payment: " + booking.getStatus());
        System.out.println("Seat status: " + showSeat.getStatus());


        Seat secondSeat = new Seat("B-4", "screen-1", "B", 4);
        ShowSeat secondShowSeat =
                new ShowSeat("show-6pm", secondSeat, new BigDecimal("12.50"));

        Booking failedBooking = bookingService.startBooking(
                "booking-2", "user-2", secondShowSeat
        );

        bookingService.failPayment(failedBooking, secondShowSeat);

        System.out.println("Failed booking: " + failedBooking.getStatus());
        System.out.println("Released seat: " + secondShowSeat.getStatus());
    }
}
