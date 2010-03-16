package no.java.portal.infrastructure;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.Status;

import no.java.portal.domain.Category;
import no.java.portal.domain.Meetings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Jun 30, 2009
 */
@Component
@Path("/moter/{category}/calendar.ics")
public class CalendarResource {

    private static final Log logger = LogFactory.getLog(CalendarResource.class);

    private Meetings meetings;

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
                    Streamer streamer = new LookICanICalStreamer(output); 
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
