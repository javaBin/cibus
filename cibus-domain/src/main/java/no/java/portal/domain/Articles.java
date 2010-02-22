package no.java.portal.domain;

import java.util.List;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 18, 2009
 */
public interface Articles {

    StaticPage getStaticPage(ArticleMetadata metadata);

    Hero getHero(ArticleMetadata metadata);

    NewsItem getNewsItem(ArticleMetadata metadata);

    List<NewsItem> getActiveArticleByCategory(List<Category> categories, int offset, int limit);
    List<NewsItem> getActiveArticleByCategory(Category nyheter, int offset, int limit);

    List<Hero> getHeroByCategory(Category category, int offset);

    List<NewsItem> getNewsItems(Category category, int year, int month);


}