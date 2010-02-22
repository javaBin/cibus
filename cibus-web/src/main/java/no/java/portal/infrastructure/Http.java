package no.java.portal.infrastructure;

import fj.F;
import fj.F2;
import fj.data.Either;
import static fj.data.Either.left;
import static fj.data.Either.right;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class Http {

    public final static F<String, Either<Exception, URL>> toUrl = new F<String, Either<Exception, URL>>() {
        public Either<Exception, URL> f(String s) {
            try {
                return right(new URL(s));
            } catch (Exception e) {
                return left(e);
            }
        }
    };

    public final static F<URL, Either<Exception, HttpURLConnection>> open = new F<URL, Either<Exception, HttpURLConnection>>() {
        public Either<Exception, HttpURLConnection> f(URL url) {

            if (!"http".equals(url.getProtocol())) {
                return left(new Exception("The URL has to be an HTTP URL."));
            }

            try {
                return right((HttpURLConnection) url.openConnection());
            } catch (Exception e) {
                return left(e);
            }
        }
    };

    public final static F2<Integer, HttpURLConnection, Either<Exception, HttpURLConnection>> checkResponseCode = new F2<Integer, HttpURLConnection, Either<Exception, HttpURLConnection>>() {
        public Either<Exception, HttpURLConnection> f(Integer responseCode, HttpURLConnection urlConnection) {
            try {
                int code = urlConnection.getResponseCode();

                if (code == responseCode) {
                    return right(urlConnection);
                }

                return left(new Exception("Invalid status code: " + code));
            } catch (Exception e) {
                return left(e);
            }
        }
    };

    public final static F<HttpURLConnection, Either<Exception, InputStream>> toInputStream = new F<HttpURLConnection, Either<Exception, InputStream>>() {
        public Either<Exception, InputStream> f(HttpURLConnection httpURLConnection) {
            try {
                return right(httpURLConnection.getInputStream());
            } catch (Exception e) {
                return left(e);
            }
        }
    };
}
