package org.example;

import javafx.fxml.LoadException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    FlightList flightList;
    BookingList bookingList;
    List<Integer> menuPath;
    Scanner in;

    String mainMenu = "1. Список рейсов\n2. Список заявок\n3. Удалить все заявки по заданному пункту отправления/назначения\n" +
            "4. Вывести все рейсы по заданным пунктам отправления и назначения и дню вылета\n" +
            "5. Вывести все заявки по заданным дате и времени вылета\n6. [Выход]",
            flightsMenu = "1. [Назад]\n2. [Добавить]",
            flightElementMenu = "1. [Назад]\n2. Удалить";

    public Menu() {
        menuPath = new ArrayList<>();
        in = new Scanner(System.in);
        flightList = MyReader.readFlights();
        bookingList = MyReader.readBookings();
    }

    public void start() {
        while (true) {
            in = new Scanner(System.in);
            clearScreen();

            if (menuPath.size() == 0) {
                section0();
            } else if (menuPath.get(0) == 6) {
                System.exit(0);
            } else {
                if (menuPath.get(0) == 1) {//Рейсы
                    if (menuPath.size() > 1) {
                        if (menuPath.get(1) == 2) section2a();//Добавить рейс
                        else section2b();//Удалить рейс
                    } else section1a();//Список всех рейсов
                } else if (menuPath.get(0) == 2) {//Заявки
                    if (menuPath.size() > 1) {
                        if (menuPath.size() > 2) {
                            if (menuPath.get(2) == 3) section3a();//Ближайшие рейсы
                            else section3b();//Рейсы с пересадками
                        } else {
                            if (menuPath.get(1) == 2) section2c();//Добавить заявку
                            else section2d();//Удалить заявку
                        }
                    } else section1b();//Список всех заявок
                } else if (menuPath.get(0) == 3) {//Удалить все заявки по заданному пункту отправления/назначения
                    if (menuPath.size() > 1) {
                        section2e();
                    } else section1c();//Настройка фильтров
                } else if (menuPath.get(0) == 4) {//Вывести все рейсы по заданным пунктам отправления и назначения и дню вылета
                    section1d();
                } else if (menuPath.get(0) == 5) {//Вывести все заявки по заданным дате и времени вылета
                    section1e();
                }
            }
        }
    }

    private void section3a() {
        BookingElement current = bookingList.get(menuPath.get(1) - 3);
        List<FlightElement> res = flightList.searchInRange(current.departureCity, current.arrivalCity, current.departureDate.format(AircraftInfo.formatter));
        if (res.isEmpty()) {
            System.out.println("Рейсов по заданным критериям не найдено");
        } else {
            System.out.println("--- Рейсы, отличающиеся по дате вылета не больше, чем на двое суток ---");
            for (FlightElement elem : res) {
                System.out.println(elem);
            }
        }
        System.out.println("1. Назад");
        int i = in.nextInt();
        if (i > 0 && i < 2) {
            menuPath.remove(2);
        }
    }

    private void section3b() {
        BookingElement current = bookingList.get(menuPath.get(1) - 3);
        List<FlightElement[]> res = flightList.searchWithTransfer(current.departureCity, current.arrivalCity);
        if (res.isEmpty()) {
            System.out.println("Рейсов по заданным критериям не найдено");
        } else {
            System.out.println("--- Рейсы с пересадками ---");
            for (FlightElement[] elem : res) {
                System.out.println(elem[0] + " & " + elem[1]);
            }
        }
        System.out.println("1. Назад");
        int i = in.nextInt();
        if (i > 0 && i < 2) {
            menuPath.remove(2);
        }
    }

    public void section0() {
        System.out.println(mainMenu);
        int i = in.nextInt();
        if (i > 0 && i < 7) {
            menuPath.add(i);
        }
    }

    public void section1a() {
        System.out.println(flightsMenu);
        for (int i = 0; i < flightList.size(); i++) {
            System.out.println((i + 3) + ". " + flightList.get(i));
        }
        int i = in.nextInt();
        if (i == 1) {
            menuPath.remove(0);
        }
        if (i == 2) {
            menuPath.add(i);
        }
        if (i > 2 && i < flightList.size() + 3) {
            menuPath.add(i);
        }
    }

    public void section1b() {
        System.out.println(flightsMenu);
        for (int i = 0; i < bookingList.size(); i++) {
            System.out.println((i + 3) + ". " + bookingList.get(i));
        }
        int i = in.nextInt();
        if (i == 1) {
            menuPath.remove(0);
        }
        if (i == 2) {
            menuPath.add(i);
        }
        if (i > 2 && i < bookingList.size() + 3) {
            menuPath.add(i);
        }
    }

    int tempDelType = -1;

    public void section1c() {
        System.out.println("--- Массовое удаление заявок ---");
        try {
            if (tempDelType == -1) {
                System.out.println("Выберите критерий поиска:\n1. По пункту отправления\n2. По пункту назначения");
                int type = in.nextInt();
                if (type > 0 && type < 3) {
                    tempDelType = type;
                }
                return;
            }
            if (tempDepCity.isEmpty()) {
                System.out.println("Введите название пункта:");
                String arr = in.nextLine();
                if (!arr.isEmpty()) {
                    tempDepCity = arr;
                    menuPath.add(1);
                }
            }
        } catch (Exception e) {}
    }

    public void section1d() {
        System.out.println("--- Поиск рейсов ---");
        try {
            if (tempDepCity.isEmpty()) {
                System.out.println("Введите место вылета:");
                String dep = in.nextLine();
                if (!dep.isEmpty()) {
                    tempDepCity = dep;
                }
                return;
            }
            if (tempArrCity.isEmpty()) {
                System.out.println("Введите место прилета:");
                String arr = in.nextLine();
                if (!arr.isEmpty()) {
                    tempArrCity = arr;
                }
                return;
            }
            if (tempDepDate.isEmpty()) {
                System.out.println("Введите дату и время вылета в формате [yyyy-MM-dd]:");
                String depDate = in.nextLine();
                if (!depDate.isEmpty()
                        && depDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    tempDepDate = depDate;
                }
            } else {
                List<FlightElement> res = flightList.searchBy(tempDepCity, tempArrCity, tempDepDate);
                if (res.isEmpty()) {
                    System.out.println("Рейсов по заданным критериям не найдено");
                } else {
                    for (FlightElement elem : res) {
                        System.out.println(elem);
                    }
                }
                System.out.println("1. Назад");
                int i = in.nextInt();
                if (i > 0 && i < 2) {
                    tempDepCity = tempArrCity = tempDepDate = "";
                    menuPath.remove(0);
                }
            }
        } catch (Exception e) {}
    }

    public void section1e() {
        System.out.println("--- Поиск заявок по дате и времени вылета ---");
        try {
            if (tempDepCity.isEmpty()) {
                System.out.println("Введите место вылета:");
                String dep = in.nextLine();
                if (!dep.isEmpty()) {
                    tempDepCity = dep;
                }
                return;
            }
            if (tempDepDate.isEmpty()) {
                System.out.println("Введите дату и время вылета в формате [yyyy-MM-dd]:");
                String depDate = in.nextLine();
                if (!depDate.isEmpty()
                        && depDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    tempDepDate = depDate;
                }
            } else {
                List<BookingElement> res = bookingList.searchBy(tempDepCity, "", tempDepDate);
                if (res.isEmpty()) {
                    System.out.println("Заявок по заданным критериям не найдено");
                } else {
                    for (BookingElement elem : res) {
                        System.out.println(elem);
                    }
                }
                System.out.println("1. Назад");
                int i = in.nextInt();
                if (i > 0 && i < 2) {
                    tempDepCity = tempDepDate = "";
                    menuPath.remove(0);
                }
            }
        } catch (Exception e) {}
    }

    public void section2e() {
        System.out.println("--- Массовое удаление заявок ---\nПункт отправления/назначения: " + tempDepCity);
        List<BookingElement> res = bookingList.searchByCity(tempDepCity, tempDelType == 2);
        for (BookingElement elem : res) {
            System.out.println(elem);
        }
        if (res.size() > 0) System.out.println("1. Отмена\n2. Удалить");
        else System.out.println("Заявок по заданным критериям не найдено\n1. Отмена");
        int i = in.nextInt();
        if (i == 1 || res.size() > 0 && i == 2) {
            if (res.size() > 0 && i == 2) {
                for (BookingElement elem : res)
                    bookingList.remove(elem);
            }
            menuPath.remove(1);
            menuPath.remove(0);
            tempDepCity = "";
            tempDelType = -1;
        }
    }

    String tempDepCity = "", tempArrCity = "", tempDepDate = "", tempArrDate = "", tempAirType = "";
    double tempPrice = -1;

    public void section2a() {
        System.out.println("--- Добавление рейса ---");
        try {
            if (tempDepCity.isEmpty()) {
                System.out.println("Введите место вылета:");
                String dep = in.nextLine();
                if (!dep.isEmpty()) {
                    tempDepCity = dep;
                }
                return;
            }
            if (tempArrCity.isEmpty()) {
                System.out.println("Введите место прилета:");
                String arr = in.nextLine();
                if (!arr.isEmpty()) {
                    tempArrCity = arr;
                }
                return;
            }
            if (tempDepDate.isEmpty()) {
                System.out.println("Введите дату и время вылета в формате [yyyy-MM-dd HH:mm]:");
                String depDate = in.nextLine();
                if (!depDate.isEmpty()
                        && depDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")
                        && AircraftInfo.parseDate(depDate).format(AircraftInfo.formatter).equals(depDate)) {
                    tempDepDate = depDate;
                }
                return;
            }
            if (tempArrDate.isEmpty()) {
                System.out.println("Введите дату и время прилета в формате [yyyy-MM-dd HH:mm]:");
                String arrDate = in.nextLine();
                if (!arrDate.isEmpty() && arrDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                    LocalDateTime arr = AircraftInfo.parseDate(arrDate);
                    if (arr.format(AircraftInfo.formatter).equals(arrDate)
                            && AircraftInfo.parseDate(tempDepDate).isBefore(arr)) {
                        tempArrDate = arrDate;
                    }
                }
                return;
            }
            if (tempAirType.isEmpty()) {
                System.out.println("Введите тип самолета:");
                String type = in.nextLine();
                if (!type.isEmpty()) {
                    tempAirType = type;
                }
                return;
            }
            if (tempPrice < 0) {
                System.out.println("Введите стоимость билета:");
                double price = in.nextDouble();
                if (price > 0) {
                    tempPrice = price;
                }
            } else {
                FlightElement newElem = new FlightElement(flightList.generateRandomId(), tempDepCity, tempArrCity, tempDepDate, tempArrDate, tempAirType, tempPrice);
                flightList.add(newElem);
                tempDepCity = tempArrCity = tempDepDate = tempArrDate = tempAirType = "";
                tempPrice = -1;
                menuPath.remove(1);
            }
        } catch (Exception e) {}
    }

    public void section2b() {
        int selectionIndex = menuPath.get(1) - 3;
        System.out.println(flightList.get(selectionIndex));
        System.out.println(flightElementMenu);
        int i = in.nextInt();
        if (i == 1) {
            menuPath.remove(1);
        }
        if (i == 2) {
            flightList.remove(selectionIndex);
            menuPath.remove(1);
        }
    }

    String tempName = "";

    public void section2c() {
        System.out.println("--- Добавление заявки ---");
        try {
            if (tempDepCity.isEmpty()) {
                System.out.println("Введите место вылета:");
                String dep = in.nextLine();
                if (!dep.isEmpty()) {
                    tempDepCity = dep;
                }
                return;
            }
            if (tempArrCity.isEmpty()) {
                System.out.println("Введите место прилета:");
                String arr = in.nextLine();
                if (!arr.isEmpty()) {
                    tempArrCity = arr;
                }
                return;
            }
            if (tempDepDate.isEmpty()) {
                System.out.println("Введите дату и время вылета в формате [yyyy-MM-dd HH:mm]:");
                String depDate = in.nextLine();
                if (!depDate.isEmpty()
                        && depDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")
                        && AircraftInfo.parseDate(depDate).format(AircraftInfo.formatter).equals(depDate)) {
                    tempDepDate = depDate;
                }
                return;
            }
            if (tempName.isEmpty()) {
                System.out.println("Введите фамилию и имя пассажира:");
                String name = in.nextLine();
                if (!name.isEmpty() && name.matches("^[\\p{L}\\s’\\-]+$")) {
                    tempName = name;
                }
            } else {
                BookingElement newElem = new BookingElement(bookingList.getFreeId(), tempDepCity, tempArrCity, tempDepDate, tempName);
                bookingList.add(newElem);
                tempDepCity = tempArrCity = tempDepDate = tempName = "";
                menuPath.remove(1);
            }
        } catch (Exception e) {}
    }

    public void section2d() {
        int selectionIndex = menuPath.get(1) - 3;
        System.out.println(bookingList.get(selectionIndex));
        System.out.println(flightElementMenu);
        System.out.println("3. Найти ближайшие рейсы\n4. Найти рейсы с пересадками");
        int i = in.nextInt();
        if (i == 1) {
            menuPath.remove(1);
        }
        if (i == 2) {
            bookingList.remove(selectionIndex);
            menuPath.remove(1);
        }
        if (i == 3 || i == 4) {
            menuPath.add(i);
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        //try {
        //    Runtime.getRuntime().exec("clear");
        //} catch (Exception e) {}
    }
}
