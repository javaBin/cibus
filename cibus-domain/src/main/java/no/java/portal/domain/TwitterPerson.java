package no.java.portal.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TwitterPerson {
    public final String name;
    public final String nick;
    public final String url;

    public TwitterPerson(String name, String nick, String url) {
        this.name = name;
        this.nick = nick;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("name", name).
            append("nick", nick).
            toString();
    }
}
