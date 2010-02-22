package no.java.portal.domain;

import java.util.List;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 18, 2009
 */
public interface Meetings {

	Meeting getMeeting(ArticleMetadata metadata);

	List<Meeting> getMeetingByCategory(Category category, int offset, int limit);

	List<Meeting> getMeetings(Category category, int year);

}