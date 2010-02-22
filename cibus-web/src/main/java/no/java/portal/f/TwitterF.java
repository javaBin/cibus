package no.java.portal.f;

import fj.F;
import fj.Unit;
import fj.F2;
import fj.data.Java;
import fj.data.List;
import fj.data.Option;
import static fj.data.Option.none;
import static fj.data.Option.some;
import no.java.portal.domain.Tweet;
import no.java.portal.domain.TwitterPerson;
import no.java.portal.domain.Twitter;
import static no.java.portal.infrastructure.OptionUtil.isSome;
import static no.java.portal.infrastructure.OptionUtil.fromSome;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TwitterF {

    private static final Pattern pattern = Pattern.compile("(.*) \\((.*)\\)");

    private static final F<Option<Tweet>, Boolean> isSome = isSome();
    private static final F<Option<Tweet>, Tweet> fromSome = fromSome();

    // -----------------------------------------------------------------------
    // Public
    // -----------------------------------------------------------------------

    public static final F2<Twitter, List<Tweet>, Unit> updateTwitterObject = new F2<Twitter, List<Tweet>, Unit>() {
        public Unit f(Twitter twitter, List<Tweet> tweetList) {
            return twitter.setJavaBinSearchTweets(tweetList);
        }
    };

    public static final F<Document<Feed>, List<Tweet>> documentToTweets = new F<Document<Feed>, List<Tweet>>() {
        public List<Tweet> f(Document<Feed> document) {

            return Java.<Entry>ArrayList_List().
                f(new ArrayList<Entry>(document.getRoot().getEntries())).
                map(toTweetOption).                 // Convert to tweets
                filter(isSome).                     // Select the successfully parsed ones
                map(fromSome);                      // Extract the values from the options
        }
    };

    // -----------------------------------------------------------------------
    // Private
    // -----------------------------------------------------------------------

    private static final F<Entry, Option<Tweet>> toTweetOption = new F<Entry, Option<Tweet>>() {
        public Option<Tweet> f(Entry entry) {
            String s = entry.getAuthor().getName().trim();

            Matcher matcher = pattern.matcher(s);

            if (matcher.matches() && matcher.groupCount() != 2) {
                return none();
            }

            String name = matcher.group(2);
            String nick = matcher.group(1);
            String url = entry.getAuthor().getUri().toString();

            return some(new Tweet(new TwitterPerson(name, nick, url), entry.getTitle()));
        }
    };
}
