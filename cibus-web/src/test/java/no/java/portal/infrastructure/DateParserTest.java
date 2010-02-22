package no.java.portal.infrastructure;

import junit.framework.TestCase;

public class DateParserTest extends TestCase {

	static String[] months = { "januar", "februar", "mars", "april", "mai",
			"juni", "juli", "august", "september", "oktober", "november",
			"desember" };

	public void test() throws Exception {
		DateParser dateParser = new DateParser();
		for (int i = 0; i < months.length; ++i) {
			String caseA = "13. " + months[i] + " 2008 kl 18";
			assertNotNull(dateParser.parse(caseA));
		}
		assertEquals("03. aug 2008 kl 07:30", DateParser.NORMAL_FMT
				.print(dateParser.parse("Fredag 3. august 2008 kl 7:30")));
		assertNotNull(dateParser.parse("Torsdag 6. mars 2008, kl 18"));
		assertNotNull(dateParser.parse("Torsdag 7 Juni 2007 kl 18.00"));
		assertNotNull(dateParser.parse("Onsdag 31. mai 2006, kl 1800-2100"));
	}
}
