package no.java.portal.infrastructure;

import fj.F;
import static fj.Function.compose;
import static fj.Function.curry;
import fj.Unit;
import fj.data.Either;
import static fj.data.Either.joinRight;
import no.java.portal.domain.Twitter;
import no.java.portal.f.AbderaF;
import static no.java.portal.f.TwitterF.documentToTweets;
import static no.java.portal.f.TwitterF.updateTwitterObject;
import static no.java.portal.infrastructure.Http.checkResponseCode;
import static no.java.portal.infrastructure.Http.open;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;

import java.io.InputStream;
import java.net.URL;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TwitterJavaBinSearchFeedUpdater implements Runnable {

    private final AbderaF abdera;
    private final URL url;
    private final Twitter twitter;

    public TwitterJavaBinSearchFeedUpdater(Abdera abdera, URL url, Twitter twitter) {
        this.abdera = new AbderaF(abdera);
        this.url = url;
        this.twitter = twitter;
    }

    public void run() {
        F<InputStream, Either<Exception, Document<Feed>>> parse = curry(abdera.parse, url.toExternalForm());

        joinRight(joinRight(joinRight(
            open.f(url).right().
            map(curry(checkResponseCode, 200))).right().
            map(Http.toInputStream)).right().
            map(parse)).
            either(handleError, compose(curry(updateTwitterObject, twitter), documentToTweets));
    }

    private F<Exception, Unit> handleError = new F<Exception, Unit>() {
        public Unit f(Exception e) {
            // TODO: log to log4j
            System.err.println("Error parsing: " + e.getMessage());
            return Unit.unit();
        }
    };
}
