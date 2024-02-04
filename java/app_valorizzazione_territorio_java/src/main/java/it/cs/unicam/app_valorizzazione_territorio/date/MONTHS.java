package it.cs.unicam.app_valorizzazione_territorio.date;

public enum MONTHS {
    JANUARY("Gennaio", 1, 31),
    FEBRUARY("Febbraio", 2, 28),
    MARCH("Marzo", 3, 31),
    APRIL("Aprile", 4, 30),
    MAY("Maggio", 5, 31),
    JUNE("Giugno", 6, 30),
    JULY("Luglio", 7, 31),
    AUGUST("Agosto", 8, 31),
    SEPTEMBER("Settembre", 9, 30),
    OCTOBER("Ottobre", 10, 31),
    NOVEMBER("Novembre", 11, 30),
    DECEMBER("Dicembre", 12, 31);

    private final String name;
    private final int number;
    private final int days;


    MONTHS(String name, int number, int days) {
        this.name = name;
        this.number = number;
        this.days = days;
    }

    public static MONTHS fromString(String name) {
        return MONTHS.valueOf(name.toUpperCase());
    }

    public static MONTHS getMonth(int number) {
        for (MONTHS month : MONTHS.values()) {
            if (month.number == number)
                return month;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public static boolean exists(int day, int month, int year) {
        if (MONTHS.getMonth(month) == null)
            return false;
        if (day < 1)
            return false;
        if (month == 2 && isLeapYear(year) && day == 29)
            return true;
        return day <= MONTHS.getMonth(month).days;
    }

    private static boolean isLeapYear(int year){
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
}
