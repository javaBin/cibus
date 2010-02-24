package no.java.portal.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 18, 2009
 */
@Repository(value = "articles")
public class ArticlesImpl implements Articles {

    private static final Log logger = LogFactory.getLog(ArticlesImpl.class);

    final PortalConfiguration configuration;
    final SimpleJdbcTemplate template;
    final ArticleMetadatas articleMetadatas;

    @Autowired
    public ArticlesImpl(@Qualifier("onpDataSource") DataSource dataSource,
                        PortalConfiguration configuration, ArticleMetadatas articleMetadatas) {
        template = new SimpleJdbcTemplate(dataSource);
        this.configuration = configuration;
        this.articleMetadatas = articleMetadatas;
    }

    public StaticPage getStaticPage(ArticleMetadata metadata) {
        String sql = "select id, name, text_value from on_article_active where id = ?";

        List<Map<String, Object>> maps = template.queryForList(sql, metadata.getId());

        String title = null;
        String body = null;

        for (Map<String, Object> map : maps) {
            String name = (String) map.get("name");

            if (name.equals("tittel")) {
                title = (String) map.get("text_value");
            } else if (name.equals("body")) {
                body = (String) map.get("text_value");
            }
        }
        return new StaticPage(body, title, metadata);
    }

    public Hero getHero(ArticleMetadata metadata) {
        String sql = "select id, name, text_value from on_article_active where id=?";
        List<Map<String, Object>> maps = template.queryForList(sql, metadata.getId());

        String navn = null;
        String heltedato = null;
        String favorittURL2Tekst = null;
        String profesjon = null;
        String bilde = null;
        String javainteresser = null;
        String hjemmeside = null;
        String favorittURL3 = null;
        String favorittURL2 = null;
        String firmaURL = null;
        String email = null;
        String kallenavn = null;
        String favorittURL1Tekst = null;
        String favorittURL1 = null;
        String firma2 = null;
        String favorittURL3Tekst = null;
        String beskrivelse = null;

        for (Map<String, Object> map : maps) {
            String name = (String) map.get("name");

            if (name.equals("Navn")) {
                navn = (String) map.get("text_value");
            } else if (name.equals("Heltedato")) {
                heltedato = (String) map.get("text_value");
            } else if (name.equals("FavorittURL2Tekst")) {
                favorittURL2Tekst = (String) map.get("text_value");
            } else if (name.equals("FavorittURL2")) {
                favorittURL2 = (String) map.get("text_value");
            } else if (name.equals("FavorittURL1Tekst")) {
                favorittURL1Tekst = (String) map.get("text_value");
            } else if (name.equals("FavorittURL1")) {
                favorittURL1 = (String) map.get("text_value");
            } else if (name.equals("FavorittURL3Tekst")) {
                favorittURL3Tekst = (String) map.get("text_value");
            } else if (name.equals("FavorittURL3")) {
                favorittURL3 = (String) map.get("text_value");
            } else if (name.equals("Profesjon")) {
                profesjon = (String) map.get("text_value");
            } else if (name.equals("Bilde")) {
                bilde = (String) map.get("text_value");
            } else if (name.equals("JavaInteresser")) {
                javainteresser = (String) map.get("text_value");
            } else if (name.equals("Hjemmeside")) {
                hjemmeside = (String) map.get("text_value");
            } else if (name.equals("FirmaURL")) {
                firmaURL = (String) map.get("text_value");
            } else if (name.equals("Email")) {
                email = (String) map.get("text_value");
            } else if (name.equals("Kallenavn")) {
                kallenavn = (String) map.get("text_value");
            } else if (name.equals("Firma2")) {
                firma2 = (String) map.get("text_value");
            } else if (name.equals("Beskrivelse")) {
                beskrivelse = (String) map.get("text_value");
            }else {
                logger.debug("Unused news item field " + name + " = " + map);
            }
        }
        return new Hero(metadata, navn, email, kallenavn, firmaURL, firma2, javainteresser, hjemmeside, profesjon, bilde,
                heltedato, favorittURL1, favorittURL1Tekst, favorittURL2, favorittURL2Tekst, favorittURL3, favorittURL3Tekst,
                beskrivelse);
    }

    public NewsItem getNewsItem(ArticleMetadata metadata) {
        String sql = "select id, name, text_value from on_article_active where id = ?";
        List<Map<String, Object>> maps = template.queryForList(sql, metadata.getId());

        String title = null;
        String author = null;
        String ingress = null;
        String text = null;
        String source = null;
        String url = null;
        String publicUrl = configuration.getContextPath() + "/nyheter/" + metadata.getId();
        List<String> otherUrls = new ArrayList<String>();

        for (Map<String, Object> map : maps) {
            String name = (String) map.get("name");

            if (name.equals("tittel")) {
                title = (String) map.get("text_value");
            } else if (name.equals("forfatter")) {
                author = (String) map.get("text_value");
            } else if (name.equals("ingress")) {
                ingress = (String) map.get("text_value");
            } else if (name.equals("br\u00f8dtekst")) {
                text = (String) map.get("text_value");
            } else if (name.equals("kilde")) {
                source = (String) map.get("text_value");
            } else if (name.equals("forfatter")) {
                author = (String) map.get("text_value");
            } else if (name.equals("url")) {
                url = (String) map.get("text_value");
            } else if (name.startsWith("ref")) {
                otherUrls.add((String) map.get("text_value"));
            } else {
                logger.debug("Unused news item field " + name + " = " + map);
            }
        }
        return new NewsItem(metadata, title, author, ingress, text, source, url, publicUrl, otherUrls);
    }

    public List<NewsItem> getActiveArticleByCategory(Category category, int offset, int limit) {
        return getActiveArticleByCategory(Arrays.asList(category), offset, limit);
    }

    public List<NewsItem> getActiveArticleByCategory(List<Category> categories, int offset, int limit) {
        List<NewsItem> articles = new ArrayList<NewsItem>(limit);
        for (ArticleMetadata metadata : articleMetadatas.getActiveArticleIdsByCategory(categories, offset, limit)) {
            articles.add(getNewsItem(metadata));
        }
        return articles;
    }

    public List<Hero> getHeroByCategory(Category category, int offset) {
        List<Hero> heroes = new ArrayList<Hero>();
        for (ArticleMetadata metadata : articleMetadatas.getActiveArticleIdsByCategory(Arrays.asList(category), offset, 1000)) {
            heroes.add(getHero(metadata));
        }
        return heroes;
    }

    public List<NewsItem> getNewsItems(Category category, int year, int month) {
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        DateTime from = JodaTimeFunctions.getDateTimeForYearAndMonth(year, month);
        for (ArticleMetadata metadata : articleMetadatas.getActiveArticleIds(category, from, from.plusMonths(1))) {
            newsItems.add(getNewsItem(metadata));
        }
        return newsItems;
    }

}
