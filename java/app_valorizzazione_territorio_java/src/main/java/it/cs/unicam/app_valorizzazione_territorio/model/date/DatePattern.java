package it.cs.unicam.app_valorizzazione_territorio.model.date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatePattern {
    public static DatePattern ITALIAN_DATE_PATTERN;

    private final Pattern pattern;
    private final String outputFormat;
    public DatePattern(String patternFormat, String outputFormat){
        if (patternFormat == null || outputFormat == null){
            throw new NullPointerException("Null parameters");
        }

        this.pattern = Pattern.compile(patternFormat);
        this.outputFormat = outputFormat;
    }

    public Matcher getMatcher(String inputString){
        return pattern.matcher(inputString);
    }

    //TODO: modify this method: in the way it is now,
    // the only possible formats are in the dd-mm-yyyy
    public String getOutput(int firstVal, int secVal, int thirdVal){
        return String.format(this.outputFormat, firstVal, secVal, thirdVal);
    }

    static {
        String monthPattern = "(0?[1-9]|1[0-2])";
        String dayPattern = "([0-2]?[0-9]|3[0-1])";
        String yearPatternSimplified = "20([0-9]{2})";
        String yearPattern = "[0-9]{4}";
        String separatorPattern = "(-|/)";

        ITALIAN_DATE_PATTERN = new DatePattern(dayPattern+separatorPattern
                +monthPattern+separatorPattern+yearPattern,
                "%02d-%02d-%04d");
    }

}
