package no.java.portal.infrastructure;

import org.joda.time.DateTime;

/**
 * Simple interface to do streaming to different formats
 * 
 * @author Thor Åge Eldby
 * @since 20. mai. 2008
 */
public interface Streamer {

	/**
	 * @param title
	 *            title of stream
	 */
	void setTitle(String title);

	/**
	 * @param id 
	 * @param start
	 * @param title
	 * @param end
	 * @param location
	 * @param description
	 */
	void write(int id, DateTime start, String title, DateTime end, String location,
			String description);

	/**
	 * Do the build completion (if it has been postponed)
	 */
	void build();

}
