package no.java.portal.infrastructure;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser handler that strips HTML
 * 
 * @author Thor Åge Eldby
 * @since 4. mars. 2008
 */
public class HtmlishHandler extends DefaultHandler {

	private StringBuffer strippedHtml = new StringBuffer();
	private String aHref;
	private boolean newline = false;

	public String getStrippedHtml() {
		return strippedHtml.toString();
	}
	
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
//		String str = new String(ch, start, length);
//		System.out.println("'" + str + "'");
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length);
		if (str.matches("[\\s]*"))
			return;
		boolean startsWith = str.matches("^[\\s]+.*");
		boolean endsWith = str.matches(".*[\\s]+$");
		str = str.trim();
		if (startsWith && newline == false)
			strippedHtml.append(' ');
		strippedHtml.append(str);
		if (endsWith)
			strippedHtml.append(' ');
//		System.out.println(startsWith + " '" + str + "' " + endsWith);
		newline = false;
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (name.equalsIgnoreCase("a")) {
			if (aHref != null) {
				newline = false;
				strippedHtml.append(" (");
				strippedHtml.append(aHref.trim());
				strippedHtml.append(")");
//				System.out.println("#HREF " + aHref);
			}
		}
		// System.out.println("#EndElem " + name);
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		newline = false;
		if (name.equalsIgnoreCase("a")) {
			aHref = atts.getValue("href");
		} else if (name.equalsIgnoreCase("br")) {
			strippedHtml.append("\n");
			newline = true;
		}
		// System.out.println("#StartElem " + name);
	}

}
