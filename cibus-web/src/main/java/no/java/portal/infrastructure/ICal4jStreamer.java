package no.java.portal.infrastructure;

import java.io.IOException;
import java.io.OutputStream;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since May 27, 2009
 */
public class ICal4jStreamer implements Streamer {

    private ComponentList components = new ComponentList();
    private OutputStream outputStream;
    private String title;

    public ICal4jStreamer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void build() {
        PropertyList propertyList = new PropertyList();
        propertyList.add(new ProdId("-//Simplicityworks//NONSGML My Product//EN"));
        propertyList.add(Version.VERSION_2_0);
        if (title != null) {
            propertyList.add(new XProperty("X-WR-CALNAME", title));
        }
        Calendar calendar = new Calendar(propertyList, components);
        CalendarOutputter outputter = new CalendarOutputter();
        try {
            outputter.output(calendar, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error writing output stream", e);
        } catch (ValidationException e) {
            throw new RuntimeException("Error validating calendar", e);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void write(int id, DateTime start, String title, DateTime end, String location, String description) {
        VEvent event = new VEvent(new net.fortuna.ical4j.model.DateTime(start.toDate()), new net.fortuna.ical4j.model.DateTime(end
                .toDate()), title);
        PropertyList properties = event.getProperties();
        properties.add(new Uid(Integer.toString(id)));
        properties.add(new Location(location));
        properties.add(new Description(description));
        components.add(event);
    }

}
