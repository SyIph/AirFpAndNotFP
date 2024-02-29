package org.example;

import java.time.LocalDateTime;

public class FlightElement extends AircraftInfo {

    protected LocalDateTime arrivalDate;
    protected String flightId, aircraftType;
    protected double ticketPrice;

    public FlightElement(String flightId, String departureCity, String arrivalCity, String departureDate, String arrivalDate, String aircraftType, double ticketPrice) {
        super(departureCity, arrivalCity, departureDate);
        this.flightId = flightId;
        this.arrivalDate = parseDate(arrivalDate, AircraftInfo.formatter);
        this.aircraftType = aircraftType;
        this.ticketPrice = ticketPrice;
    }

    public FlightElement(String flightId, String departureCity, String arrivalCity, LocalDateTime departureDate, LocalDateTime arrivalDate, String aircraftType, double ticketPrice) {
        super(departureCity, arrivalCity, departureDate);
        this.flightId = flightId;
        this.arrivalDate = arrivalDate;
        this.aircraftType = aircraftType;
        this.ticketPrice = ticketPrice;
    }

    public String toString(FlightElement elem) {
        return "[" + elem.flightId
                + "] " + elem.departureDate.format(formatter)
                + ", " + elem.departureCity
                + " --> " + elem.arrivalCity;
    }

    public FlightElement copy() {
        return new FlightElement(flightId, departureCity, arrivalCity, departureDate, arrivalDate, aircraftType, ticketPrice);
    }
}
