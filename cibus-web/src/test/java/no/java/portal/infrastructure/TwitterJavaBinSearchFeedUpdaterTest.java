package no.java.portal.infrastructure;

import fj.F;
import fj.P;
import fj.P1;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import junit.framework.TestCase;
import no.java.portal.domain.Tweet;
import no.java.portal.f.AbderaF;
import no.java.portal.f.TwitterF;
import org.apache.abdera.Abdera;
import static org.codehaus.plexus.PlexusTestCase.getTestFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TwitterJavaBinSearchFeedUpdaterTest extends TestCase {
    public void testParsing() throws Exception {
        final AbderaF abdera = new AbderaF(new Abdera());

        final File testFile = getTestFile("src/test/resources/search.atom");

        Either<? extends Throwable, P1<Option<List<Tweet>>>> e = IO.runIo(testFile, new F<InputStream, P1<Option<List<Tweet>>>>() {
            public P1<Option<List<Tweet>>> f(InputStream inputStream) {

                List<Tweet> tweetList = abdera.parse.f("file://blah", inputStream).right().toOption().
                    map(TwitterF.documentToTweets).some();

                return P.p(Option.some(tweetList));
            }
        });

//        Matcher matcher = Pattern.compile("(.*) \\((.*)\\)").matcher("whatskarbodoing (Jaran)");
//        matcher.find();
//        System.out.println(matcher.groupCount());
//        System.out.println("matcher.group(0) = " + matcher.group(1));
//        System.out.println("matcher.group(1) = " + matcher.group(2));

//        e.left().value().printStackTrace();
        if(!e.isRight()){
            e.left().value().printStackTrace();
        }
        assertTrue(e.isRight());
        assertTrue(e.right().value()._1().isSome());
        List<Tweet> list = e.right().value()._1().some();
        assertEquals(15, list.length());

        Tweet tweet = list.head();
        assertEquals("finalizing presentation for tomorrow's javaBin - \"Software Agents with JADE\" at UiA Krsand @ 18:00", tweet.getText());
        assertEquals("Jaran", tweet.getPerson().name);
        assertEquals("whatskarbodoing", tweet.getPerson().nick);
    }
}
