package no.java.portal.infrastructure;

import fj.F;
import fj.P1;
import fj.data.Either;
import static fj.data.Either.right;
import static fj.data.Either.left;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class IO {
    public static <A> Either<? extends Throwable, P1<A>> runIo(File file, F<InputStream, P1<A>> f) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            return right(f.f(inputStream));
        } catch (Throwable e) {
            return left(e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /*
    public static <A> Either<? extends Throwable, P1<A>> runIo(final File file, final F<InputStream, P1<A>> f) {
        final FileInputStream[] inputStream = new FileInputStream[]{null};

        return new Callable<P1<A>>() {
            public P1<A> call() throws Exception {
                try {
                    inputStream[0] = new FileInputStream(file);
                    return f.f(inputStream[0]);
                } catch (Throwable e) {
                    return left(e);
                }
                finally {
                    if (inputStream[0] != null) {
                        try {
                            inputStream[0].close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        }
    }
*/
}
