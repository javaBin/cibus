package no.java.portal.infrastructure;

import fj.F;
import fj.data.Option;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class OptionUtil {
    public static <A> F<Option<A>, Boolean> isSome() {
        return new F<Option<A>, Boolean>() {
            public Boolean f(Option<A> option) {
                return option.isSome();
            }
        };
    }

    public static <A> F<Option<A>, A> fromSome() {
        return new F<Option<A>, A>() {
            public A f(Option<A> option) {
                return option.some();
            }
        };
    }
}
