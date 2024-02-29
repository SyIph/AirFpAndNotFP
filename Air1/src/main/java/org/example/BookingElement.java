package org.example;

public class BookingElement extends AircraftInfo {
    protected String bookingId, passengerName;

    public BookingElement(String bookingId, String departureCity, String arrivalCity, String departureDate, String passengerName) {
        super(departureCity, arrivalCity, departureDate);
        this.bookingId = bookingId;
        this.passengerName = passengerName;
    }

    public String toString() {
        return "[" + this.bookingId + "] " + this.passengerName + ", " + this.departureDate.format(formatter) + ", " + this.departureCity + " --> " + this.arrivalCity;
    }

}
