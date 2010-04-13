package no.java.portal.infrastructure;

import org.joda.time.DateTime;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
public class Event {
    
    private final DateTime start;
    private final DateTime end;
    private final String summary;
    private final String location;
    private final String description;

    public Event(DateTime start, DateTime end, String summary, String location, String description) {
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.location = location;
        this.description = description;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public String getSummary() {
        return summary;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

}
