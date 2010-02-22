package no.java.portal.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(value = "meetings")
public class MeetingsImpl implements Meetings {

    final SimpleJdbcTemplate template;
    final PortalConfiguration configuration;
    final ArticleMetadatas articleMetadatas;

    @Autowired
    public MeetingsImpl(DataSource dataSource, PortalConfiguration configuration, ArticleMetadatas articleMetadatas) {
        template = new SimpleJdbcTemplate(dataSource);
        this.configuration = configuration;
        this.articleMetadatas = articleMetadatas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.java.portal.domain.MeetingsIF#getMeeting(no.java.portal.domain.ArticleMetadata)
     */
    public Meeting getMeeting(ArticleMetadata metadata) {
        String sql = "select id, name, text_value from on_article_active where id = ?";

        List<Map<String, Object>> maps = template.queryForList(sql, metadata.getId());

        String title = null;
        String ingress = null;
        String place = null;
        String time = null;
        String publicUrl = configuration.getContextPath() + "/moter/" + metadata.getId();

        for (Map<String, Object> map : maps) {
            String name = (String) map.get("name");

            if (name.equals("tittel")) {
                title = (String) map.get("text_value");
            } else if (name.equals(("sted"))) {
                place = (String) map.get("text_value");
            } else if (name.equals("tid")) {
                time = (String) map.get("text_value");
            } else if (name.equals("intro")) {
                ingress = (String) map.get("text_value");
            }
        }
        // TODO text-innholdet er forelopig satt til null, da jeg ikke fant det i data-dumpen fra ONP.
        return new Meeting(metadata, title, time, ingress, null, place, publicUrl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.java.portal.domain.MeetingsIF#getMeetingByCategory(no.java.portal.domain.Category, int, int)
     */
    public List<Meeting> getMeetingByCategory(Category category, int offset, int limit) {
        List<Meeting> meetings = new ArrayList<Meeting>(limit);
        for (ArticleMetadata metadata : articleMetadatas.getActiveArticleIdsByCategory(Arrays.asList(category), offset, limit)) {
            meetings.add(getMeeting(metadata));
        }
        return meetings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.java.portal.domain.MeetingsIF#getMeetings(no.java.portal.domain.Category, int)
     */
    public List<Meeting> getMeetings(Category category, int year) {
        List<Meeting> meetings = new ArrayList<Meeting>();
        DateTime from = JodaTimeFunctions.getDateTimeForYear(year);
        for (ArticleMetadata metadata : articleMetadatas.getActiveArticleIds(category, from, from.plusYears(1))) {
            meetings.add(getMeeting(metadata));
        }
        return meetings;
    }
}
