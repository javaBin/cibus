package no.java.portal.infrastructure;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate icalendar events
 *
 * @author Thor Åge Eldby
 * @since 23. juli. 2007
 */
public class ICalendarFactory {

    /**
     * See http://en.wikipedia.org/wiki/UTF-8
     */
    private static final byte UTF8_B0 = (byte) 0xc0; // 110xxxxx

    private static final byte THREE_BITS = (byte) 0xe0; // 11100000

    private static final byte UTF8_B1 = (byte) 0x80; // 10xxxxxx

    private static final byte TWO_BITS = (byte) 0xc0; // 11000000

    /**
     * Max bytes on one line is 75; and taking CRLF from that becomes 73
     */
    private static final int MAX_LINE_LENGTH = 75 - 2;

    private static final byte[] CRLF = new byte[]{0x0d, 0x0a};

    private static final String CRLF_STRING;

    private static final String LF_STRING;

    static {
        try {
            CRLF_STRING = new String(CRLF, "UTF-8");
            LF_STRING = new String(new byte[]{0x0a}, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding UTF-8 unsupported", e);
        }
    }

    private DateTimeFormatter format = DateTimeFormat.forPattern(
            "yyyyMMdd'T'HHmmss'Z'").withZone(DateTimeZone.UTC);

    private StringBuffer tmpBuf;

    private final String title;
    private final List<Event> events = new ArrayList<Event>();


    private ICalendarFactory(String title) {
        this.title = title;
    }

    public static ICalendarFactory newNamedInstance(String title) {
        return new ICalendarFactory(title);
    }

    public static ICalendarFactory newInstance() {
        return new ICalendarFactory(null);
    }

    /**
     * @return ical event as byte array in UTF-8 format
     */
    public byte[] create() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        appendLine(baos, "BEGIN:VCALENDAR");
        appendLine(baos, "VERSION:2.0");
        appendLine(baos, "PRODID:-//Simplicityworks//NONSGML My Product//EN");
        if (title != null) {
            append("X-WR-CALNAME;CHARSET=UTF-8:");
            appendLine(baos, title);
        }
        for (Event event : events) {
            appendLine(baos, "BEGIN:VEVENT");
            append("DTSTART:");
            appendLine(baos, format.print(event.getStart()));
            append("DTEND:");
            appendLine(baos, format.print(event.getEnd()));
            String summary = event.getSummary();
            if (summary != null && summary.trim().length() > 0) {
                append("SUMMARY;CHARSET=UTF-8:");
                appendLine(baos, summary);
            }
            String location = event.getLocation();
            if (location != null && location.trim().length() > 0) {
                append("LOCATION;CHARSET=UTF-8:");
                appendLine(baos, location);
            }
            String description = event.getDescription();
            if (description != null && description.trim().length() > 0) {
                append("DESCRIPTION;CHARSET=UTF-8:");
                appendLine(baos, description);
            }
            appendLine(baos, "END:VEVENT");
        }

        appendLine(baos, "END:VCALENDAR");
        return baos.toByteArray();
    }

    private void append(String string) {
        if (tmpBuf == null) {
            tmpBuf = new StringBuffer(string);
        } else {
            tmpBuf.append(string);
        }
    }

    private void appendLine(OutputStream os, String string) {
        try {
            append(string);
            String line = tmpBuf.toString();
            line = line.replace(CRLF_STRING, "\\n");
            line = line.replace(LF_STRING, "\\n");
            byte[] bs = line.getBytes("UTF-8");
            writeLine(os, bs, MAX_LINE_LENGTH);
            tmpBuf = null;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Missing UTF-8 support", e);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to stream", e);
        }
    }

    /**
     * Splits lines to fit into maxlength. If line is longer it is split with a
     * CRLF (not in maxLength calculation). Will split earlier to not split at
     * double-byte chars.
     * <p/>
     * OBS: Does not do three or four byte char split correctly.
     *
     * @param os        stream
     * @param bs        bytes
     * @param maxLength TODO
     * @throws IOException error writing
     */
    static protected void writeLine(OutputStream os, byte[] bs, int maxLength)
            throws IOException {
        int pos = 0;
        boolean firstLine = true;
        while (bs.length - pos > maxLength) {
            int newPos = pos + maxLength;
            // Check that we're not splitting a two byte UTF-8 char
            if (newPos > 0 && newPos < bs.length
                    && (bs[newPos - 1] & THREE_BITS) == UTF8_B0
                    && (bs[newPos] & TWO_BITS) == UTF8_B1) {
                --newPos;
            }
            os.write(bs, pos, newPos - pos);
            os.write(CRLF);
            os.write(' ');
            if (firstLine) {
                firstLine = false;
                --maxLength;
            }
            pos = newPos;
        }
        if (bs.length != pos) {
            os.write(bs, pos, bs.length - pos);
            os.write(CRLF);
        }
    }

    public void addEvent(Event event) {
        events.add(event);
    }

}
