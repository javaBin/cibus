package no.java.portal.infrastructure;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author Thor Åge Eldby
 * @see ICalendarFactory
 * @since 23. juli. 2007
 */
public class ICalendarFactoryTest {

    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";

    @Test
    public void testCreate() throws Exception {
        DateTimeFormatter format = DateTimeFormat.forPattern(DATE_FORMAT);

        ICalendarFactory factory = ICalendarFactory.newInstance();
        factory.addEvent(new Event(
                format.parseDateTime("14.07.1997 19:00"),
                format.parseDateTime("15.07.1997 05:59"),
                "Dette er en dag de feirer der borte i "
                        + "Frankrike for det var da de tok bastiljen fra rojalistene. "
                        + "Dette feires med fest også videre og hvem vet hva annet. "
                        + "\nKongen er ikke invitert ettersom han mistet hodet i "
                        + "det kritiske øyeblikket.",
                "Oslo",
                null));
        assertEqualsByteArray(getFileBytes("test.ics"), factory.create());

        factory = ICalendarFactory.newInstance();
        factory.addEvent(new Event(
                format.parseDateTime("14.12.1997 18:00"),
                format.parseDateTime("15.12.1997 04:59"),
                null,
                null,
                null));
        assertEqualsByteArray(getFileBytes("test2.ics"), factory.create());
    }

    protected byte[] getFileBytes(String string) throws IOException {
        InputStream is = getClass().getResourceAsStream(string);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return baos.toByteArray();
    }

    protected void assertEqualsByteArray(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals("At pos " + i + " more of expected \""
                    + string(expected, i) + "\", more of actual \""
                    + string(actual, i) + "\"", expected[i], actual[i]);
        }
    }

    private String string(byte[] bs, int i) {
        try {
            int len = 20;
            if (len + i > bs.length)
                len = bs.length - i;
            return new String(bs, i, len, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testWriteLine() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Check a string that fits the length of two lines exactly
        ICalendarFactory.writeLine(baos, "abc".getBytes("UTF-8"), 2);
        byte[] bs = baos.toByteArray();
        assertEquals(8, bs.length);
        assertEquals('a', (char) bs[0]);
        assertEquals('b', (char) bs[1]);
        assertEquals('c', (char) bs[5]);
        // Check splitting of two byte character avoided
        baos = new ByteArrayOutputStream();
        ICalendarFactory.writeLine(baos, "aaå".getBytes("UTF-8"), 3);
        assertEquals(13, baos.toByteArray()[2]);
        // Check that space on second line is considered correctly
        baos = new ByteArrayOutputStream();
        ICalendarFactory.writeLine(baos, "abcd".getBytes("UTF-8"), 2);
        bs = baos.toByteArray();
        assertEquals(12, bs.length);
        assertEquals('c', (char) bs[5]);
        assertEquals('d', (char) bs[9]);
    }

}
