package org.example;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FlightList {

    private List<FlightElement> flightElements = new ArrayList<>();

    public void add(FlightElement elem) {
        flightElements.add(elem);
    }

    public FlightElement get(int index) {
        return flightElements.get(index);
    }

    public void remove(int index) {
        flightElements.remove(index);
    }

    public String generateRandomId() {
        Random r = new Random();
        String id = "";
        do {
            id = "";
            id += Character.toUpperCase((char)(r.nextInt(26) + 'a'));
            id += Character.toUpperCase((char)(r.nextInt(26) + 'a'));
            id += String.format("%03d", r.nextInt(999));
        } while (containId(id));
        return id;
    }

    private boolean containId(String id) {
        for (FlightElement elem : flightElements) {
            if (elem.flightId.equals(id)) return true;
        }
        return false;
    }

    public List<FlightElement> searchBy(String depCity, String arrCity, String depDate) {
        List<FlightElement> res = new ArrayList<>();
        for (FlightElement elem : flightElements) {
            if ((depCity.isEmpty() || elem.departureCity.equals(depCity)) &&
                    (arrCity.isEmpty() || elem.arrivalCity.equals(arrCity)) &&
                    (depDate.isEmpty() || elem.departureDate.format(AircraftInfo.formatter2).equals(depDate)))
                res.add(elem);
        }
        return res;
    }

    protected boolean checkInDaysRange(LocalDateTime date1, LocalDateTime date2, int days) {
        long duration  = date2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - date1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInDays <= days;
    }

    public List<FlightElement> searchInRange(String depCity, String arrCity, String depDate) {
        List<FlightElement> res = new ArrayList<>();
        for (int i = -2; i < 3; i++) {
            LocalDateTime date = AircraftInfo.parseDate(depDate).plusDays(i);
            List<FlightElement> toCheck = searchBy(depCity, arrCity, date.format(AircraftInfo.formatter2));
            for (FlightElement elem : toCheck) {
                if (checkInDaysRange(date, elem.departureDate, 2)) {
                    res.add(elem);
                }
            }
        }
        return res;
    }

    public List<FlightElement[]> searchWithTransfer(String depCity, String arrCity) {
        List<FlightElement[]> res = new ArrayList<>();
        for (FlightElement elem1 : flightElements) {
            if (!elem1.departureCity.equals(depCity)) continue;
            for (FlightElement elem2 : flightElements) {
                if (!elem2.arrivalCity.equals(arrCity)) continue;
                if (elem1.arrivalCity.equals(elem2.departureCity)) {
                    if (checkInDaysRange(elem1.arrivalDate, elem2.departureDate, 1))
                        res.add(new FlightElement[] {elem1, elem2});
                }
            }
        }
        return res;
    }

    public int size() {
        return flightElements.size();
    }
}
