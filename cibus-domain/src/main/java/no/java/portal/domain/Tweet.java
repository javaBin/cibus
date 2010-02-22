package no.java.portal.domain;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class Tweet {
    private TwitterPerson person;
    private String text;

    public Tweet(TwitterPerson person, String text) {
        this.person = person;
        this.text = text;
    }

    public TwitterPerson getPerson() {
        return person;
    }

    public String getText() {
        return text;
    }
}
