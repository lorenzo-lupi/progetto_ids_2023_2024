package it.cs.unicam.app_valorizzazione_territorio.date;

import it.cs.unicam.app_valorizzazione_territorio.exceptions.IllegalDateException;

/**
 * A class representing a date. This class was made to provide a not-deprecated alternative to the
 * Date class, which is now deprecated.
 */
public class Date implements Comparable<Date>{
    private final DatePattern pattern;
    private final int day;
    private final int month;
    private final int year;

    /**
     * Creates a new Date object.
     * @param day an int representing the day
     * @param month an int representing the month
     * @param year an int representing the year
     */
    public Date(int day, int month, int year, DatePattern pattern){
        this(Integer.toString(day)
                + "-" +
                Integer.toString(month)
                + "-" +
                Integer.toString(year), pattern);
    }

    /**
     * Creates a new Date object using the italian pattern.
     */
    public Date(String regex){
        this(regex, DatePattern.ITALIAN_DATE_PATTERN);
    }


    /**
     * Creates a new Date object.
     * @param regex a String representing a date in the format dd-mm-yyyy
     */
    public Date(String regex, DatePattern pattern){
        if(regex == null || pattern == null)
            throw new IllegalArgumentException("Date and pattern must not be null");

        this.pattern = pattern;
        if(!pattern.getMatcher(regex).matches())
            throw new IllegalDateException("Invalid date format");

        String[] values = regex.split("(/|-)");

        this.day = Integer.parseInt(values[0]);
        this.month = Integer.parseInt(values[1]);
        this.year = Integer.parseInt(values[2]);

        if(!MONTHS.exists(day, month, year))
            throw new IllegalDateException("Date does not exist");
    }



    @Override
    public String toString(){
        return pattern.getOutput(day, month, year);
    }

    @Override
    public int hashCode(){
        int day = (this.day & 0x1F) << (15+4);
        int month = (this.month & 0xF) << 15;
        int year = (this.year & 0x7FFF);
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
