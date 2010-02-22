package no.java.portal.f;

import fj.F2;
import fj.data.Either;
import static fj.data.Either.right;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;

import java.io.InputStream;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class AbderaF {
    private final Parser parser;

    public AbderaF(Abdera abdera) {
        this.parser = abdera.getParser();
    }

    public final F2<String, InputStream, Either<Exception, Document<Feed>>> parse = new F2<String, InputStream, Either<Exception, Document<Feed>>>() {
        public Either<Exception, Document<Feed>> f(String url, InputStream inputStream) {
            return right(parser.<Feed>parse(inputStream, url));
        }
    };
}
