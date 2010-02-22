package no.java.portal.infrastructure;

import java.text.DateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Parses some dates rather loosely
 * 
 * @author Thor Åge Eldby
 * @since 31. mars. 2008
 */
public class DateParser {

    static final String DATE_FMT = "dd'.' MMM yyyy 'kl' HH:mm";

    static final Locale norwegianLocale = new Locale("NO", "no");
    /**
     * Normal norwegian date time formatter
     * <p>
     * Unlike the JDK {@link DateFormat} this is thread-safe so we need only one
     */
    static final public DateTimeFormatter NORMAL_FMT = DateTimeFormat.forPattern(DATE_FMT).withLocale(norwegianLocale);

    /**
     * Time formatter
     */
    static final public DateTimeFormatter ONLY_TIME_FMT = DateTimeFormat.forPattern("HH:mm").withLocale(norwegianLocale);

    Pattern p = Pattern
            .compile("^(([Mm]andag|[Tt]irsdag|[Oo]nsdag|[Tt]orsdag|[Ff]redag|[Ll]ørdag|[Ss]øndag)\\s*)?([0-3]?[0-9])[\\.]?\\s*"
                    + "([jJ]anuar|[fF]ebruar|[mM]ars|[aA]pril|[mM]ai|[jJ]uni|[jJ]uli|[aA]ugust|[sS]eptember|[oO]ktober|[nN]ovember|[dD]esember)"
                    + "\\s*(20[0-9][0-9])?(,*)?\\s*(kl[\\.]?\\s*)?\\s*([0-2]?[0-9])?[:.]?([0-9][0-9])?((\\s*-\\s*[0-2]?[0-9])[:.]?([0-9][0-9])?)?$");

    /**
     * @param txt
     *            text to parse
     * @return date object
     */
    public DateTime parse(String txt) {
        txt = txt.trim();
        Matcher matcher = p.matcher(txt);
        if (!matcher.find() || matcher.groupCount() != 12)
            throw new RuntimeException("Hææ: " + txt);
        txt = matcher.group(3);
        String day = matcher.group(3);
        String month = matcher.group(4);
        String year = matcher.group(5);
        String hour = matcher.group(8);
        // Unlikely time; so that someone can take responsibility; yeah you
        // yeah, I'm talking to you
        if (hour == null)
            hour = "08";
        String min = matcher.group(9);
        if (min == null)
            min = "00";
        if (year == null) {
            year = String.valueOf(new DateTime().year().get());
            // System.out.println("year = " + year);
        }
        txt = day + ". " + month.toLowerCase() + " " + year + " kl " + hour + ":" + min;
         System.out.println("txt: " + txt);

        return NORMAL_FMT.parseDateTime(txt);
    }
}
