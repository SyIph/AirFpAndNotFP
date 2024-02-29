package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class AircraftInfo {

    protected LocalDateTime departureDate;
    protected String arrivalCity, departureCity;

    public AircraftInfo(String departureCity, String arrivalCity, String departureDate) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = parseDate(departureDate);
    }

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH),
            formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);//2022-08-01 13:30
    protected static LocalDateTime parseDate(String sDate) {
        LocalDateTime date = LocalDateTime.parse(sDate, formatter);
        return date;
    }

}
