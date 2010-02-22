package no.java.portal.servlet.twitter;

import no.java.portal.domain.Twitter;
import no.java.portal.domain.Tweet;
import no.java.portal.servlet.CibusSimpleTagSupport;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.ArrayList;

import fj.data.Java;
import fj.data.List;
import fj.F;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class SearchTwitterTag extends CibusSimpleTagSupport {

    private String term;

    private String var;

    private final static F<List<Tweet>, ArrayList<Tweet>> listF = Java.List_ArrayList();

    public void doTag() throws JspException, IOException {

        if (!"javaBin".equals(term)) {
            throw new RuntimeException("'javabin' is the only legal search term");
        }

        Twitter twitter = getBean("twitter", Twitter.class);

        getJspContext().setAttribute(var, listF.f(twitter.getJavaBinSearchTweets()));
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
