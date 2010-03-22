package no.java.portal.infrastructure;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class LookICanICalStreamer implements Streamer {
    
    private OutputStream outputStream;

    public LookICanICalStreamer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setTitle(String title) {
    }

    public void write(int id, DateTime start, String title, DateTime end, String location, String description) {
        ICalendarFactory cf = new ICalendarFactory();
        cf.setStart(start);
        cf.setEnd(end);
        cf.setSummary(title);
        cf.setLocation(location);
        cf.setDescription(description);
        try {
            outputStream.write(cf.create());
        } catch (IOException e) {
            throw new RuntimeException("Error writing to stream", e);
        }
    }

    public void build() {
    }

}
