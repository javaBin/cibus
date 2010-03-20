package no.java.portal.resource;

import no.java.portal.domain.*;
import no.java.portal.infrastructure.*;
import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.*;
import java.io.*;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Jun 30, 2009
 */
@Component
@Path("/moter/{category}/calendar.ics")
public class CalendarResource {

    private static final Log logger = LogFactory.getLog(CalendarResource.class);

    private final Meetings meetings;

    @Autowired
    public CalendarResource(Meetings meetings) {
        this.meetings = meetings;
    }

    @GET
    @Produces("text/calendar")
    public StreamingOutput getCalendar(@PathParam("category") final String categoryString) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    ICal4jStreamer streamer = new ICal4jStreamer(output);
                    EventBuilder eventBuilder = new EventBuilder();
                    Category category = Category.valueOf(categoryString);
                    eventBuilder.buildEvent(meetings, category, streamer);
                } catch (RuntimeException e) {
                    logger.error("Error creating calendar for " + categoryString, e);
                    throw new WebApplicationException(e, Status.SERVICE_UNAVAILABLE);
                }
            }
        };
    }
}
