package org.example;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MyReader {
    public static List<FlightElement> readFlights() {
        List<FlightElement> flightList = new ArrayList<>();
        try {
            FileReader reader = new FileReader("src/main/java/org/example/data/flights.json");
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jObj = (JSONObject)obj;
                FlightElement elem = new FlightElement((String) jObj.get("flight_number"), (String)jObj.get("departure_city"),
                        (String)jObj.get("arrival_city"), (String)jObj.get("departure_datetime"),
                        (String)jObj.get("arrival_datetime"), (String)jObj.get("aircraft_type"), (double)jObj.get("ticket_price"));
                flightList.add(elem);
            }
        } catch (Exception e) {e.printStackTrace();}
        return flightList;
    }

    public static List<BookingElement> readBookings() {
        List<BookingElement> bookingList = new ArrayList<>();
        try {
            FileReader reader = new FileReader("src/main/java/org/example/data/bookings.json");
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jObj = (JSONObject)obj;
                BookingElement elem = new BookingElement((String)jObj.get("booking_id"), (String)jObj.get("departure_city"),
                        (String)jObj.get("arrival_city"), (String)jObj.get("departure_datetime"),
                        (String)jObj.get("passenger_name"));
                bookingList.add(elem);
            }
        } catch (Exception e) {e.printStackTrace();}
        return bookingList;
    }
}
