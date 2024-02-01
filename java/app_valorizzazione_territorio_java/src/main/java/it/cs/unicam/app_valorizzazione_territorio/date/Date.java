package it.cs.unicam.app_valorizzazione_territorio.date;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalDateException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date implements Comparable<Date>{
    private static final Pattern DATE_PATTERN_ITA = Pattern.compile("([0-2]?[0-9]|3[0-1])-(0?[1-9]|1[0-2])-20([0-9]{2})");
    private final int day;
    private final int month;
    private final int year;

    /**
     * Creates a new Date object.
     * @param regex a String representing a date in the format dd-mm-yyyy
     */
    public Date(String regex){
        if(regex == null)
            throw new IllegalArgumentException("Date must not be null");

        Matcher matcher = DATE_PATTERN_ITA.matcher(regex);

        if(!matcher.matches())
            throw new IllegalArgumentException("Invalid date");

        String[] values = regex.split("-");

        this.day = Integer.parseInt(values[0]);
        this.month = Integer.parseInt(values[1]);
        this.year = Integer.parseInt(values[2]);

        if(!MONTHS.exists(day, month, year))
            throw new IllegalDateException("Date does not exist");
    }

    public Date(int day, int month, int year){
        if(!MONTHS.exists(day, month, year))
            throw new IllegalDateException("Date does not exist");
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString(){
        return String.format("%02d-%02d-%04d", day, month, year);
    }

    @Override
    public int hashCode(){
        int day = (this.day & 0x1F) << (12+4);
        int month = (this.month & 0xF) << 12;
        int year = (this.year & 0xFFF);
        return day | month | year;
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!(o instanceof Date other))
            return false;

        return this.day == other.day && this.month == other.month && this.year == other.year;
    }

    @Override
    public int compareTo(Date o) {
        if(o == null)
            throw new IllegalArgumentException("Date must not be null");
        if(this.year < o.year)
            return -1;
        if(this.year > o.year)
            return 1;
        if(this.month < o.month)
            return -1;
        if(this.month > o.month)
            return 1;
        return Integer.compare(this.day, o.day);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
