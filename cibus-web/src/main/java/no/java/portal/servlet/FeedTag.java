package no.java.portal.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;

import no.java.portal.infrastructure.FeedUpdater;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.rss.Item;

/**
 * Fetches RSS (as {@link Item}) or Atom (as {@link Entry}) feed list
 *
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since 13. jan. 2009
 */
public class FeedTag extends CibusSimpleTagSupport {

	private int count = 5;
	private String var;

	private String feedBean;

	@Override
	public void doTag() throws JspException, IOException {
		FeedUpdater fu = getBean(feedBean, FeedUpdater.class);
		List<WireFeed> items = fu.getItems();
        getJspContext().setAttribute(var, items.subList(0, Math.min(count, items.size())));
    }

	public void setCount(int count) {
		this.count = count;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setFeedBean(String feedBean) {
		this.feedBean = feedBean;
	}

}
