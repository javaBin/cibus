package no.java.portal.infrastructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import no.java.portal.domain.Category;
import no.java.portal.domain.Meeting;
import no.java.portal.domain.Meetings;

import org.joda.time.DateTime;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Builds up
 * 
 * @author Thor Åge Eldby
 * @since 20. mai. 2008
 */
public class EventBuilder {

    private static final String XHTML_LAT1 = readFile("xhtml-lat1.ent");
    private static final String XHTML_SPECIAL = readFile("xhtml-special.ent");
    private static final String XHTML_SYMBOL = readFile("xhtml-symbol.ent");

    /**
     * @param meetings
     * @param category
     * @param streamer
     */
    public void buildEvent(Meetings meetings, Category category, Streamer streamer) {
        streamer.setTitle("javaBin-møter for " + category.getName());
        List<Meeting> ms = meetings.getMeetingByCategory(category, 0, 10);
        DateParser dateParser = new DateParser();
        for (Meeting m : ms) {
            int id = m.getId();
            String startTime = m.getTime();
            DateTime start;
            if (startTime != null && startTime.trim().length() > 0) {
                start = dateParser.parse(startTime);
            } else
                continue;
            String title = m.getTitle();
            DateTime end = start.plusHours(3);
            String location = m.getPlace();
            String description = convertHTMLToText(m.getIngress());
            streamer.write(id, start, title, end, location, description);
        }
        streamer.build();
    }

    /**
     * @param html
     * @return text
     */
    protected static String convertHTMLToText(String html) {
        StringBuffer buf = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<!DOCTYPE htmlish [\n");
        buf.append(XHTML_LAT1);
        buf.append(XHTML_SPECIAL);
        buf.append(XHTML_SYMBOL);
        buf.append("]>\n");
        // + " <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0
        // Transitional//EN\"
        // \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        buf.append("<div>");
        html = html.replaceAll("<[pP] *>", "<p\\/>");
        html = html.replaceAll("<\\/[pP]>", "");
        html = html.replaceAll("<[bB][rR]>", "<br\\/>");
        html = html.replaceAll("<\\/[bB][rR]>", "");
        html = html.replaceAll("&([^a-zA-Z0-9])", "&amp;$1");
        html = html.replaceAll("&([a-zA-Z0-9]+[^;])", "&amp;$1");
        buf.append(html);
        buf.append("</div>");
        try {
            XMLReader prsr = XMLReaderFactory.createXMLReader();
            // System.out.println("Parser: " + prsr.getClass().getName());
            HtmlishHandler hh = new HtmlishHandler();
            prsr.setContentHandler(hh);
            prsr.parse(new InputSource(new ByteArrayInputStream(buf.toString().getBytes())));
            return hh.getStrippedHtml();
        } catch (SAXException e) {
            // System.out.println(html);
            throw new RuntimeException("Error parsing " + html, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading", e);
        }
    }

    /**
     * @param fileName
     * @return content
     */
    protected static String readFile(String fileName) {
        try {
            InputStream is = EventBuilder.class.getResourceAsStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1)
                baos.write(buf, 0, len);
            return baos.toString("UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Error reading from stream", e);
        }
    }

}
