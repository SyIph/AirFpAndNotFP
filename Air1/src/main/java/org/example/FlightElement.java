package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class FlightElement extends AircraftInfo {

    protected LocalDateTime arrivalDate;
    protected String flightId, aircraftType;
    protected double ticketPrice;

    public FlightElement(String flightId, String departureCity, String arrivalCity, String departureDate, String arrivalDate, String aircraftType, double ticketPrice) {
        super(departureCity, arrivalCity, departureDate);
        this.flightId = flightId;
        this.arrivalDate = parseDate(arrivalDate);
        this.aircraftType = aircraftType;
        this.ticketPrice = ticketPrice;
    }

    public String toString() {
        return "[" + this.flightId + "] "
                + this.departureDate.format(formatter)
                + ", " + this.departureCity + " --> "
                + this.arrivalCity;
    }

}
