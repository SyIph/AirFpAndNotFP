package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class AircraftInfo {
    protected LocalDateTime departureDate; // Данные
    protected String arrivalCity, departureCity; // Данные
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH), // Данные
            formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH); // Данные

    public AircraftInfo(String departureCity, String arrivalCity, String departureDate) { // Действие
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = parseDate(departureDate, formatter);
    }

    public AircraftInfo(String departureCity, String arrivalCity, LocalDateTime departureDate) { // Действие
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
    }

    protected static LocalDateTime parseDate(String sDate, DateTimeFormatter formatter) { // Вычисление
        LocalDateTime date = LocalDateTime.parse(sDate, formatter);
        return date;
    }

}
