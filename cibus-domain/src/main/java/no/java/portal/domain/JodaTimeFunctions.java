package no.java.portal.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JodaTimeFunctions {

    private static DateTimeFormatter shortDateTimeFormatter = new DateTimeFormatterBuilder().
            appendDayOfMonth(0).
            appendLiteral(". ").
            appendMonthOfYearText().
            toFormatter();

    private static DateTimeFormatter longDateTimeFormatter = new DateTimeFormatterBuilder().
            appendDayOfMonth(0).
            appendLiteral(". ").
            appendMonthOfYearText().
            appendLiteral(" ").
            appendYear(4, 4).
            appendLiteral(", ").
            appendHourOfDay(1).
            appendLiteral(":").
            appendMinuteOfHour(2).
            toFormatter();

    public static String toShortString(LocalDateTime dateTime) {
        return shortDateTimeFormatter.print(dateTime);
    }

    public static String toLongString(LocalDateTime dateTime) {
        return longDateTimeFormatter.print(dateTime);
    }

    public static List<DateTime> getYears(DateTime first, DateTime last) {
        if (last.isBefore(first)) {
            throw new RuntimeException("Last date can't be prior to first");
        }
        DateTime mark = getDateTimeForYear(first.getYear());
        ArrayList<DateTime> list = new ArrayList<DateTime>();
        while (mark.isBefore(last)) {
            list.add(mark);
            mark = getDateTimeForYear(mark.getYear() + 1);
        }
        return list;
    }

    public static DateTime getDateTimeForYear(int year) {
        return new DateTime(year, 1, 1, 0, 0, 0, 0);
    }

    public static DateTime getDateTimeForYearAndMonth(int year, int month) {
        return new DateTime(year, month, 1, 0, 0, 0, 0);
    }
}
