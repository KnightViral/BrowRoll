package sample;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    private final static int START_YEAR = 1940;
    private final static int END_YEAR = 2020;
    private final static String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final static int NAME_MIN_LENGTH = 3;
    private final static int NAME_MAX_LENGTH = 7;

    public static Date getRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = getRandomBetween(START_YEAR, END_YEAR);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = getRandomBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return Date.from(gc.toZonedDateTime().toInstant());
    }

    public static int getRandomBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public static int getRandomTo(int to) {
        return getRandomBetween(0, to);
    }

    public static int getHours(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinute(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getDays(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getYears(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String generateName() {
        char[] alphaNumArray = ALPHANUM.toCharArray();
        int nameLength =getRandomBetween(NAME_MIN_LENGTH,NAME_MAX_LENGTH);
        char[] pool = new char[nameLength];
        for (int i = 0; i <nameLength; i++)
            pool[i] = alphaNumArray[getRandomTo(alphaNumArray.length-1)];
        return new String(pool);
    }
}
