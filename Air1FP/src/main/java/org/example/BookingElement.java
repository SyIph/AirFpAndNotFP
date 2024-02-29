package org.example;

import java.time.LocalDateTime;

public class BookingElement extends AircraftInfo {
    protected String bookingId, passengerName;

    public BookingElement(String bookingId, String departureCity, String arrivalCity, String departureDate, String passengerName) {
        super(departureCity, arrivalCity, departureDate);
        this.bookingId = bookingId;
        this.passengerName = passengerName;
    }

    public BookingElement(String bookingId, String departureCity, String arrivalCity, LocalDateTime departureDate, String passengerName) {
        super(departureCity, arrivalCity, departureDate);
        this.bookingId = bookingId;
        this.passengerName = passengerName;
    }

    public String toString(BookingElement elem) {
        return "[" + elem.bookingId + "] " + elem.passengerName + ", " + elem.departureDate.format(formatter) + ", " + elem.departureCity + " --> " + elem.arrivalCity;
    }

    public BookingElement copy() {
        return new BookingElement(bookingId, departureCity, arrivalCity, departureDate, passengerName);
    }
}
