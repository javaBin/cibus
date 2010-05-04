package no.java.portal.infrastructure;

import net.fortuna.ical4j.data.CalendarBuilder;
import no.java.portal.domain.Category;
import no.java.portal.domain.Meetings;
import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since May 27, 2009
 */
@Tags("local")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring.xml" })
@Ignore
public class EventBuilderTest {

    @Autowired
    private Meetings meetings;

    @Test
    public void testReadAndWriteICals_Oslo() throws Exception {
        buildEvents(Category.moter_oslo);
    }

    @Test
    public void testReadAndWriteICals_Stavanger() throws Exception {
        buildEvents(Category.moter_stavanger);
    }

    @Test
    public void testReadAndWriteICals_Trondheim() throws Exception {
        buildEvents(Category.moter_trondheim);
    }

    @Test
    public void testReadAndWriteICals_Sorlandet() throws Exception {
        buildEvents(Category.moter_sorlandet);
    }

    @Test
    public void testReadAndWriteICals_Bergen() throws Exception {
        buildEvents(Category.moter_bergen);
    }

    @Test
    public void testReadAndWriteICals_Tromso() throws Exception {
        buildEvents(Category.moter_tromso);
    }

    private void buildEvents(Category category) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new EventBuilder().buildEvent(meetings, category, new LookICanICalStreamer(baos));
        // Try to parse
        CalendarBuilder builder = new CalendarBuilder();
        byte[] bs = baos.toByteArray();
        builder.build(new InputStreamReader(new ByteArrayInputStream(bs)));
    }

}
