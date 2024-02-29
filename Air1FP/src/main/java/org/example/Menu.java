package org.example;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Menu {
    static String[][] MenuTexts = new String[][] {
            new String[] {"1. Список рейсов\n2. Список заявок\n3. Удалить все заявки по заданному пункту отправления/назначения\n4. Вывести все рейсы по заданным пунктам отправления и назначения и дню вылета\n5. Вывести все заявки по заданным дате и времени вылета\n6. [Выход]", "1. [Назад]\n2. Добавить", "1. [Назад]\n2. Удалить", "\n3. Найти ближайшие рейсы\n4. Найти рейсы с пересадками"},
            new String[] {"--- Добавление заявки ---", "Введите место вылета:", "Введите место прилета:", "Введите дату и время вылета в формате [yyyy-MM-dd HH:mm]:", "Введите фамилию и имя пассажира:"},
            new String[] {"--- Добавление рейса ---", "Введите место вылета:", "Введите место прилета:", "Введите дату и время вылета в формате [yyyy-MM-dd HH:mm]:", "Введите дату и время прилета в формате [yyyy-MM-dd HH:mm]:", "Введите тип самолета:", "Введите стоимость билета:"},
            new String[] {"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}","\\d{4}-\\d{2}-\\d{2}","^[\\p{L}\\s’\\-]+$"},
            new String[] {"--- Массовое удаление заявок ---\nПункт отправления/назначения: ", "1. Отмена\n2. Удалить", "Заявок по заданным критериям не найдено\n1. Отмена"},
            new String[] {"Заявок по заданным критериям не найдено", "1. Назад"},
            new String[] {"--- Поиск заявок по дате и времени вылета ---", "Введите место вылета:", "Введите дату и время вылета в формате [yyyy-MM-dd]:"},
            new String[] {"Рейсов по заданным критериям не найдено", "1. Назад"},
            new String[] {"--- Поиск рейсов ---", "Введите место вылета:", "Введите место прилета:", "Введите дату и время вылета в формате [yyyy-MM-dd]:"},
            new String[] {"--- Массовое удаление заявок ---", "Выберите критерий поиска:\n1. По пункту отправления\n2. По пункту назначения", "Введите название пункта:"},
            new String[] {"Рейсов по заданным критериям не найдено", "--- Рейсы с пересадками ---", "1. Назад"},
            new String[] {"Рейсов по заданным критериям не найдено", "--- Рейсы, отличающиеся по дате вылета не больше, чем на двое суток ---", "1. Назад"}
    };

    public Menu() {}

    public void start(String[][] menuText) {
        List<FlightElement> flightList = MyReader.readFlights();
        List<BookingElement> bookingList = MyReader.readBookings();
        List<Integer> menuPath = new ArrayList<>();
        while (true) {
            Scanner in = new Scanner(System.in);
            clearScreen();
            if (menuPath.size() == 0) {
                int inputRes = sectionMainMenuConsole(in, menuText[0][0]);
                if (inputRes > 0 && inputRes < 7)
                    menuPath = menuPathAdd(menuPath, inputRes);
            } else if (menuPath.get(0) == 6) {
                System.exit(0);
            } else {
                if (menuPath.get(0) == 1) {
                    if (menuPath.size() > 1) {
                        if (menuPath.get(1) == 2) {
                            Object[] inputRes = sectionAddFlightConsole(menuText[2], menuText[3]);
                            FlightElement newElem = new FlightElement(flightListGenRandomId(flightList), (String) inputRes[0], (String) inputRes[1], (String) inputRes[2], (String) inputRes[3], (String) inputRes[4], (Double) inputRes[5]);
                            flightList = flightListAdd(flightList, newElem);
                            menuPath = menuPathRemove(menuPath, 1);
                        } else {
                            int selectionIndex = menuPath.get(1) - 3,
                                    inputRes = sectionDeleteFlightConsole(flightList.get(selectionIndex), menuText[0][2], in);
                            if (inputRes == 1 || inputRes == 2) {
                                if (inputRes == 2)
                                    flightList = flightListRemove(flightList, selectionIndex);
                                menuPath = menuPathRemove(menuPath, 1);
                            }
                        }
                    } else {
                        int inputRes = sectionFlightListConsole(menuText[0][1], in, flightList);
                        menuPath = sectionFlightListLogic(inputRes, menuPath, flightList);
                    }
                } else if (menuPath.get(0) == 2) {
                    if (menuPath.size() > 1) {
                        if (menuPath.size() > 2) {
                            BookingElement current = bookingList.get(menuPath.get(1) - 3);
                            if (menuPath.get(2) == 3) {
                                /*Logic*/List<FlightElement> res = flightListSearchInRange(flightList, current.departureCity, current.arrivalCity, current.departureDate.format(AircraftInfo.formatter));
                                /*Console*/int inputRes = sectionUpcomingFlightsConsole(res, in, menuText[11]);
                                /*Logic*/if (inputRes == 1) menuPath = menuPathRemove(menuPath, 2);
                            } else {
                                /*Logic*/List<FlightElement[]> res = flightListSearchWithTransfer(flightList, current.departureCity, current.arrivalCity);
                                /*Console*/int inputRes = sectionFlightsWithTransfersConsole(res, in, menuText[10]);
                                /*Logic*/if (inputRes == 1) menuPath = menuPathRemove(menuPath, 2);
                            }
                        } else {
                            if (menuPath.get(1) == 2) {
                                String[] inputRes = sectionAddBookingConsole(menuText[1], menuText[3]);
                                BookingElement newElem = new BookingElement(bookingListGetFreeId(bookingList), inputRes[0], inputRes[1], inputRes[2], inputRes[3]);
                                bookingList = bookingListAdd(bookingList, newElem);
                                menuPath = menuPathRemove(menuPath, 1);
                            } else {
                                int selectionIndex = menuPath.get(1) - 3;
                                int inputRes = sectionDeleteBookingConsole(bookingList.get(selectionIndex), menuText[0][2] + menuText[0][3], in);
                                if (inputRes == 1)
                                    menuPath = menuPathRemove(menuPath, 1);
                                else if (inputRes == 2) {
                                    bookingList = bookingListRemove(bookingList, selectionIndex);
                                    menuPath = menuPathRemove(menuPath, 1);
                                } else if (inputRes == 3 || inputRes == 4)
                                    menuPath = menuPathAdd(menuPath, inputRes);
                            }
                        }
                    } else {
                        int inputRes = sectionBookingListConsole(menuText[0][1], in, bookingList);
                        menuPath = sectionBookingListLogic(inputRes, menuPath, bookingList);
                    }
                } else if (menuPath.get(0) == 3) {
                    Object[] inputRes = sectionFilterByDepartureOrArrivalCityConsole(menuText[9]);
                    List<BookingElement> res = bookingListSearchByCity(bookingList, (String) inputRes[1], (int) inputRes[0] == 2);
                    int inputRes2 = sectionDeleteByDepartureOrArrivalCityConsole(res, in, (String) inputRes[1], menuText[4]);
                    if (inputRes2 == 1 || res.size() > 0 && inputRes2 == 2) {
                        if (res.size() > 0 && inputRes2 == 2) {
                            for (BookingElement elem : res) {
                                bookingList = bookingListRemove(bookingList, elem);
                            }
                        }
                        menuPath = menuPathRemove(menuPath, 0);
                    }
                } else if (menuPath.get(0) == 4) {
                    String[] inputRes = sectionFilterByDepArrCityAndDay(menuText[8], menuText[3]);
                    List<FlightElement> res = flightListSearchBy(flightList, inputRes[0], inputRes[1], inputRes[2]);
                    int i = sectionFilterByDepArrCityAndDayFinal(res, in, menuText[7]);
                    if (i == 1)
                        menuPath = menuPathRemove(menuPath, 0);
                } else if (menuPath.get(0) == 5) {
                    String[] inputRes = sectionFilterByDepartureCityAndDay(menuText[6], menuText[3]);
                    List<BookingElement> res = bookingListSearchBy(bookingList, inputRes[0], "", inputRes[1]);
                    int i = sectionFilterByDepartureCityAndDayFinal(res, in, menuText[5]);
                    if (i == 1)
                        menuPath = menuPathRemove(menuPath, 0);
                }
            }
        }
    }

    private int sectionUpcomingFlightsConsole(List<FlightElement> res, Scanner in, String[] menuText) {
        try{
            if (res.isEmpty()) {
                System.out.println(menuText[0]);
            } else {
                System.out.println(menuText[1]);
                for (FlightElement elem : res)
                    System.out.println(elem.toString(elem));
            }
            System.out.println(menuText[2]);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    private int sectionFlightsWithTransfersConsole(List<FlightElement[]> res, Scanner in, String[] menuText) {
        try{
            if (res.isEmpty()) {
                System.out.println(menuText[0]);
            } else {
                System.out.println(menuText[1]);
                for (FlightElement[] elem : res) {
                    System.out.println(elem[0].toString(elem[0]) + " & " + elem[1].toString(elem[1]));
                }
            }
            System.out.println(menuText[2]);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public int sectionMainMenuConsole(Scanner in, String menuText) {
        try{
            System.out.println(menuText);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public int sectionFlightListConsole(String menuText, Scanner in, List<FlightElement> flightList) {
        try {
            System.out.println(menuText);
            for (int i = 0; i < flightList.size(); i++) {
                System.out.println((i + 3) + ". " + flightList.get(i).toString(flightList.get(i)));
            }
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public List<Integer>  sectionFlightListLogic(int inputRes, List<Integer> menuPath, List<FlightElement> flightList) {
        if (inputRes == 1)
            return menuPathRemove(menuPath, 0);
        else if (inputRes == 2)
            return menuPathAdd(menuPath, inputRes);
        else if (inputRes > 2 && inputRes < flightList.size() + 3)
            return menuPathAdd(menuPath, inputRes);
        return menuPath;
    }

    public int sectionBookingListConsole(String menuText, Scanner in, List<BookingElement> bookingList) {
        try{
            System.out.println(menuText);
            for (int i = 0; i < bookingList.size(); i++) {
                System.out.println((i + 3) + ". " + bookingList.get(i).toString(bookingList.get(i)));
            }
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public List<Integer> sectionBookingListLogic(int inputRes, List<Integer> menuPath,
                                                 List<BookingElement> bookingList) {
        if (inputRes == 1)
            return menuPathRemove(menuPath, 0);
        else if (inputRes == 2)
            return menuPathAdd(menuPath, inputRes);
        else if (inputRes > 2 && inputRes < bookingList.size() + 3)
            return menuPathAdd(menuPath, inputRes);
        return menuPath;
    }

    public Object[] sectionFilterByDepartureOrArrivalCityConsole(String[] menuText) {
        System.out.println(menuText[0]);
        int type = -1;
        String city = "";
        while (true) {
            Scanner in = new Scanner(System.in);
            try {
                if (type < 1 || type > 2) {
                    System.out.println(menuText[1]);
                    type = in.nextInt();
                } else if (city.isEmpty()) {
                    System.out.println(menuText[2]);
                    city = in.nextLine();
                } else {
                    return new Object[] {type, city};
                }
            } catch (Exception ignored) {}
        }
    }

    public String[] sectionFilterByDepArrCityAndDay(String[] menuText, String[] regex) {
        System.out.println(menuText[0]);
        String depCity = "", arrCity = "", depDate = "";
        while (true) {
            Scanner in = new Scanner(System.in);
            if (depCity.isEmpty()) {
                System.out.println(menuText[1]);
                depCity = in.nextLine();
            } else if (arrCity.isEmpty()) {
                System.out.println(menuText[2]);
                arrCity = in.nextLine();
            } else if (depDate.isEmpty() || !depDate.matches(regex[1])) {
                System.out.println(menuText[3]);
                depDate = in.nextLine();
            } else {
                return new String[] {depCity, arrCity, depDate};
            }
        }
    }

    public int sectionFilterByDepArrCityAndDayFinal(List<FlightElement> res, Scanner in, String[] menuText) {
        try {
            if (res.isEmpty())
                System.out.println(menuText[0]);
            else
                for (FlightElement elem : res)
                    System.out.println(elem.toString(elem));
            System.out.println(menuText[1]);
            return in.nextInt();
        } catch (Exception ignore) {}
        return 1;
    }

    public String[] sectionFilterByDepartureCityAndDay(String[] menuText, String[] regex) {
        System.out.println(menuText[0]);
        String depCity = "", depDate = "";
        while (true) {
            Scanner in = new Scanner(System.in);
            if (depCity.isEmpty()) {
                System.out.println(menuText[1]);
                depCity = in.nextLine();
            } else if (depDate.isEmpty() || !depDate.matches(regex[1])) {
                System.out.println(menuText[2]);
                depDate = in.nextLine();
            } else {
                return new String[] {depCity, depDate};
            }
        }
    }

    public int sectionFilterByDepartureCityAndDayFinal(List<BookingElement> res, Scanner in, String[] menuText) {
        try {
            if (res.isEmpty()) {
                System.out.println(menuText[0]);
            } else {
                for (BookingElement elem : res)
                    System.out.println(elem.toString(elem));
            }
            System.out.println(menuText[1]);
            return in.nextInt();
        } catch (Exception ignore) {}
        return 1;
    }

    public int sectionDeleteByDepartureOrArrivalCityConsole(List<BookingElement> res, Scanner in, String city, String[] menuText) {
        try {
            System.out.println(menuText[0] + city);
            for (BookingElement elem : res)
                System.out.println(elem.toString(elem));
            if (res.size() > 0)
                System.out.println(menuText[1]);
            else
                System.out.println(menuText[2]);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }


    public Object[] sectionAddFlightConsole(String[] addFlightMenu, String[] regex) {
        System.out.println(addFlightMenu[0]);
        String depCity = "", arrCity = "", depDate = "", arrDate = "", airType = "";
        double price = -1;
        while (true) {
            Scanner in = new Scanner(System.in);
            try {
                if (depCity.isEmpty()) {
                    System.out.println(addFlightMenu[1]);
                    depCity = in.nextLine();
                } else if (arrCity.isEmpty()) {
                    System.out.println(addFlightMenu[2]);
                    arrCity = in.nextLine();
                } else if (depDate.isEmpty() || !depDate.matches(regex[0]) || !AircraftInfo.parseDate(depDate, AircraftInfo.formatter).format(AircraftInfo.formatter).equals(depDate)) {
                    System.out.println(addFlightMenu[3]);
                    depDate = in.nextLine();
                } else if (arrDate.isEmpty() || !arrDate.matches(regex[0])) {
                    System.out.println(addFlightMenu[4]);
                    arrDate = in.nextLine();
                } else {
                    LocalDateTime arr = AircraftInfo.parseDate(arrDate, AircraftInfo.formatter);
                    if (!arr.format(AircraftInfo.formatter).equals(arrDate) || !AircraftInfo.parseDate(depDate, AircraftInfo.formatter).isBefore(arr)) {
                        System.out.println(addFlightMenu[4]);
                        arrDate = in.nextLine();
                    } else if (airType.isEmpty()) {
                        System.out.println(addFlightMenu[5]);
                        airType = in.nextLine();
                    } else if (price < 0) {
                        System.out.println(addFlightMenu[6]);
                        price = in.nextDouble();
                    } else {
                        return new Object[] {depCity, arrCity, depDate, arrDate, airType, price};
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    public int sectionDeleteFlightConsole(FlightElement elem, String menuText, Scanner in) {
        try {
            System.out.println(elem.toString(elem));
            System.out.println(menuText);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public String[] sectionAddBookingConsole(String[] addBookingMenu, String[] regex) {
        System.out.println(addBookingMenu[0]);
        String depCity = "", arrCity = "", depDate = "", name = "";
        while (true) {
            Scanner in = new Scanner(System.in);
            try {
                if (depCity.isEmpty()) {
                    System.out.println(addBookingMenu[1]);
                    depCity = in.nextLine();
                } else if (arrCity.isEmpty()) {
                    System.out.println(addBookingMenu[2]);
                    arrCity = in.nextLine();
                } else if (depDate.isEmpty() || !depDate.matches(regex[0]) || !AircraftInfo.parseDate(depDate, AircraftInfo.formatter).format(AircraftInfo.formatter).equals(depDate)) {
                    System.out.println(addBookingMenu[3]);
                    depDate = in.nextLine();
                } else if (name.isEmpty() || !name.matches(regex[2])) {
                    System.out.println(addBookingMenu[4]);
                    name = in.nextLine();
                } else {
                    return new String[] {depCity,arrCity,depDate,name};
                }
            } catch (Exception ignored) {}
        }
    }

    public int sectionDeleteBookingConsole(BookingElement header, String menuText, Scanner in) {
        try {
            System.out.println(header);
            System.out.println(menuText);
            return in.nextInt();
        } catch (Exception ignored) {}
        return -1;
    }

    public static void clearScreen() { // Действие
        //Якобы работает кроссплатформенно, но не проверено т.к. не работает в терминале Intellij Idea
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public List<Integer> menuPathAdd(List<Integer> menuPath, int i) {
        List<Integer> clone = new ArrayList<>(menuPath);
        clone.add(i);
        return clone;
    }

    public List<Integer> menuPathRemove(List<Integer> menuPath, int i) {
        List<Integer> clone = new ArrayList<>(menuPath);
        clone.remove(i);
        return clone;
    }

    ///////////////////////////////////////

    public List<FlightElement> flightListAdd(List<FlightElement> list, FlightElement elem) {
        List<FlightElement> copy = flightListCopy(list);
        copy.add(elem);
        return copy;
    }

    public List<FlightElement> flightListRemove(List<FlightElement> list, int index) {
        List<FlightElement> copy = flightListCopy(list);
        copy.remove(index);
        return copy;
    }

    public String flightListGenRandomId(List<FlightElement> list) {
        Random r = new Random();
        String id;
        do {
            id = "";
            id += Character.toUpperCase((char)(r.nextInt(26) + 'a'));
            id += Character.toUpperCase((char)(r.nextInt(26) + 'a'));
            id += String.format("%03d", r.nextInt(999));
        } while (flightListContainId(list, id));
        return id;
    }

    private boolean flightListContainId(List<FlightElement> list, String id) {
        for (FlightElement elem : list) {
            if (elem.flightId.equals(id)) return true;
        }
        return false;
    }

    public List<FlightElement> flightListSearchBy(List<FlightElement> list, String depCity, String arrCity, String depDate) {
        List<FlightElement> res = new ArrayList<>();
        for (FlightElement elem : list) {
            if ((depCity.isEmpty() || elem.departureCity.equals(depCity)) &&
                    (arrCity.isEmpty() || elem.arrivalCity.equals(arrCity)) &&
                    (depDate.isEmpty() || elem.departureDate.format(AircraftInfo.formatter2).equals(depDate)))
                res.add(elem);
        }
        return res;
    }

    protected boolean flightListCheckInDaysRange(LocalDateTime date1, LocalDateTime date2, int days) {
        long duration  = Math.abs(date2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - date1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInDays <= days;
    }

    public List<FlightElement> flightListSearchInRange(List<FlightElement> list, String depCity, String arrCity, String depDate) {
        List<FlightElement> res = new ArrayList<>();
        for (int i = -2; i < 3; i++) {
            LocalDateTime date = AircraftInfo.parseDate(depDate, AircraftInfo.formatter).plusDays(i);
            List<FlightElement> toCheck = flightListSearchBy(list, depCity, arrCity, date.format(AircraftInfo.formatter2));
            for (FlightElement elem : toCheck) {
                if (flightListCheckInDaysRange(date, elem.departureDate, 2)) {
                    res.add(elem);
                }
            }
        }
        return res;
    }

    public List<FlightElement[]> flightListSearchWithTransfer(List<FlightElement> list, String depCity, String arrCity) {
        List<FlightElement[]> res = new ArrayList<>();
        for (FlightElement elem1 : list) {
            if (!elem1.departureCity.equals(depCity)) continue;
            for (FlightElement elem2 : list) {
                if (!elem2.arrivalCity.equals(arrCity)) continue;
                if (elem1.arrivalCity.equals(elem2.departureCity)) {
                    if (flightListCheckInDaysRange(elem1.arrivalDate, elem2.departureDate, 1))
                        res.add(new FlightElement[] {elem1, elem2});
                }
            }
        }
        return res;
    }

    public List<FlightElement> flightListCopy(List<FlightElement> list) {
        List<FlightElement> copy = new ArrayList<>();
        for (FlightElement elem : list) {
            FlightElement newElem = elem.copy();
            copy.add(newElem);
        }
        return copy;
    }

    ///////////////////////////////////////

    public List<BookingElement> bookingListAdd(List<BookingElement> list, BookingElement elem) {
        List<BookingElement> copy = bookingListCopy(list);
        copy.add(elem);
        return copy;
    }

    public List<BookingElement> bookingListRemove(List<BookingElement> list, int index) {
        List<BookingElement> copy = bookingListCopy(list);
        copy.remove(index);
        return copy;
    }

    public List<BookingElement> bookingListRemove(List<BookingElement> list, BookingElement elem) {
        List<BookingElement> copy = bookingListCopy(list);
        for (BookingElement elem1 : copy) {
            if (elem.arrivalCity.equals(elem1.arrivalCity) && elem.departureCity.equals(elem1.departureCity) &&
            elem.bookingId.equals(elem1.bookingId) && elem.departureDate.equals(elem1.departureDate) &&
            elem.passengerName.equals(elem1.passengerName)) {
                copy.remove(elem1);
                break;
            }
        }
        return copy;
    }

    public String bookingListGetFreeId(List<BookingElement> list) {
        int counter = 1;
        String id;
        do {
            counter++;
            id = "BK" + String.format("%03d", counter);
        } while (bookingListContainId(list, id));
        return id;
    }

    private boolean bookingListContainId(List<BookingElement> list, String id) {
        for (BookingElement elem : list) {
            if (elem.bookingId.equals(id)) return true;
        }
        return false;
    }

    public List<BookingElement> bookingListSearchByCity(List<BookingElement> list, String city, boolean isArrivalCity) {
        List<BookingElement> res = new ArrayList<>();
        for (BookingElement elem : list) {
            if (!isArrivalCity && elem.departureCity.equals(city) || isArrivalCity&&elem.arrivalCity.equals(city))
                res.add(elem);
        }
        return res;
    }

    public List<BookingElement> bookingListSearchBy(List<BookingElement> list, String depCity, String arrCity, String depDate) {
        List<BookingElement> res = new ArrayList<>();
        for (BookingElement elem : list) {
            if ((depCity.isEmpty() || elem.departureCity.equals(depCity)) &&
                    (arrCity.isEmpty() || elem.arrivalCity.equals(arrCity)) &&
                    (depDate.isEmpty() || elem.departureDate.format(AircraftInfo.formatter2).equals(depDate)))
                res.add(elem);
        }
        return res;
    }

    public List<BookingElement> bookingListCopy(List<BookingElement> list) {
        List<BookingElement> copy = new ArrayList<>();
        for (BookingElement elem : list) {
            BookingElement newElem = elem.copy();
            copy.add(newElem);
        }
        return copy;
    }
}
