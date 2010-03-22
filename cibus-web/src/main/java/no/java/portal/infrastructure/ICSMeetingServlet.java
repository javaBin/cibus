package no.java.portal.infrastructure;

import no.java.portal.domain.Category;
import no.java.portal.domain.Meetings;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since May 27, 2009
 */
@Deprecated // Is this in use anymore? CalendarResource does its job now doesn't it?
public class ICSMeetingServlet extends CibusServletBase {

    private static final long serialVersionUID = -2877768829328802995L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String categoryName = req.getRequestURI().replaceFirst(".*/", "").replaceAll("\\.ics$", "");
            Category category = Category.valueOf(categoryName);
            resp.setContentType("text/calendar");
            Streamer streamer = new LookICanICalStreamer(resp.getOutputStream());
            EventBuilder eventBuilder = new EventBuilder();
            eventBuilder.buildEvent(getBean("meetings", Meetings.class), category, streamer);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/plain");
            ServletOutputStream os = resp.getOutputStream();
            e.printStackTrace(new PrintStream(os));
        }
    }
}
