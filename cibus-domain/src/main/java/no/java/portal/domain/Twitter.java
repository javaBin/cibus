package no.java.portal.domain;

import fj.data.List;
import static fj.data.List.nil;
import fj.Unit;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component("twitter")
public class Twitter {
    private List<Tweet> javaBinSearchTweets = nil();

    public List<Tweet> getJavaBinSearchTweets() {
        return javaBinSearchTweets;
    }

    public Unit setJavaBinSearchTweets(List<Tweet> javaBinSearchTweets) {
        this.javaBinSearchTweets = javaBinSearchTweets;
        return Unit.unit();
    }
}
