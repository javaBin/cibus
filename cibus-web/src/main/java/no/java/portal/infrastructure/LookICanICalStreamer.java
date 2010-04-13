package no.java.portal.infrastructure;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class LookICanICalStreamer implements Streamer {

    private OutputStream outputStream;
    private ICalendarFactory cf;

    public LookICanICalStreamer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setTitle(String title) {
        cf = ICalendarFactory.newNamedInstance(title);
    }

    public void write(int id, DateTime start, String summary, DateTime end, String location, String description) {
        cf.addEvent(new Event(start, end, summary, location, description));
    }

    public void build() {
        try {
            outputStream.write(cf.create());
        } catch (IOException e) {
            throw new RuntimeException("Error writing to stream", e);
        }
    }

}
