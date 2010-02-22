package no.java.portal.domain;

import static junit.framework.Assert.assertEquals;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;

@RunWith(JUnit4ClassRunner.class)
public class JodaTimeFunctionsTest {

    @Test
    public void getYears() {
        DateTime someTime = new DateTime("2008-01-01T00:30");
        {
            List<DateTime> years = JodaTimeFunctions.getYears(someTime, someTime);
            assertEquals(1, years.size());
            assertEquals(2008, years.iterator().next().getYear());
        }
        DateTime backThen = new DateTime("2006-08-22");
        {
            List<DateTime> years = JodaTimeFunctions.getYears(backThen, someTime);
            assertEquals(3, years.size());
            Iterator<DateTime> yIt = years.iterator();
            assertEquals(2006, yIt.next().getYear());
            assertEquals(2007, yIt.next().getYear());
            assertEquals(2008, yIt.next().getYear());
        }
    }

    @Test
    public void getDateTimeForYear() {
        DateTime year = JodaTimeFunctions.getDateTimeForYear(2008);
        assertEquals(2008, year.getYear());
        year = year.minusMillis(1);
        assertEquals(2007, year.getYear());
    }
}
