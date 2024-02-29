package org.example;

import java.util.ArrayList;
import java.util.List;

public class BookingList {
    private List<BookingElement> bookingElements = new ArrayList<>();

    public void add(BookingElement elem) {
        bookingElements.add(elem);
    }

    public BookingElement get(int index) {
        return bookingElements.get(index);
    }

    public void remove(int index) {
        bookingElements.remove(index);
    }

    public void remove(BookingElement elem) {
        bookingElements.remove(elem);
    }

    public String getFreeId() {
        int counter = 1;
        String id = "";
        do {
            counter++;
            id = "BK" + String.format("%03d", counter);
        } while (containId(id));
        return id;
    }

    private boolean containId(String id) {
        for (BookingElement elem : bookingElements) {
            if (elem.bookingId.equals(id)) return true;
        }
        return false;
    }

    public int size() {
        return bookingElements.size();
    }

    public List<BookingElement> searchByCity(String city, boolean isArrivalCity) {
        List<BookingElement> res = new ArrayList<>();
        for (BookingElement elem : bookingElements) {
            if (!isArrivalCity && elem.departureCity.equals(city) || isArrivalCity&&elem.arrivalCity.equals(city))
                res.add(elem);
        }
        return res;
    }

    public List<BookingElement> searchBy(String depCity, String arrCity, String depDate) {
        List<BookingElement> res = new ArrayList<>();
        for (BookingElement elem : bookingElements) {
            if ((depCity.isEmpty() || elem.departureCity.equals(depCity)) &&
                    (arrCity.isEmpty() || elem.arrivalCity.equals(arrCity)) &&
                    (depDate.isEmpty() || elem.departureDate.format(AircraftInfo.formatter2).equals(depDate)))
                res.add(elem);
        }
        return res;
    }

}
