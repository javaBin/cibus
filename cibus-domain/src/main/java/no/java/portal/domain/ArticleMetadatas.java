package no.java.portal.domain;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 18, 2009
 */
public interface ArticleMetadatas {

    public abstract List<ArticleMetadata> getActiveArticleIdsByCategory(List<Category> categories, int offset, int limit);

    public abstract List<ArticleMetadata> getActiveArticleIds(Category category, DateTime from, DateTime to);

    public abstract ArticleMetadata getArticle(int id);

    /**
     * @return list of all years with meetings
     */
    public abstract List<DateTime> getYears();

}