package no.java.portal.infrastructure;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 */
public class FeedUpdater implements Runnable {

	private URL feedURL;
	public List<WireFeed> items = Collections.emptyList();

	public FeedUpdater(URL feedURL) {
		this.feedURL = feedURL;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			WireFeed feed = new WireFeedInput().build(new XmlReader(feedURL));
			if (feed instanceof Channel) {
				items = ((Channel) feed).getItems();
			} else if (feed instanceof Feed) {
				items = ((Feed) feed).getEntries();
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Error in feed arguments", e);
        } catch (FeedException e) {
			throw new RuntimeException("Error parsing feed", e);
        } catch (Throwable e) {
            e.printStackTrace();
        }
	}

	public List<WireFeed> getItems() {
		return items;
	}

}
