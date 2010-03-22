package no.java.portal;

import no.java.portal.domain.Articles;
import no.java.portal.domain.Category;
import no.java.portal.domain.NewsItem;
import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class ArticlesTest {

    @Autowired
    Articles articles;

    @Test
    @Ignore
    public void foo() {
        List<NewsItem> newsItems = this.articles.getActiveArticleByCategory(Category.nyheter, 0, 10);
        assertNotNull(newsItems);

        for (NewsItem article : newsItems) {
            System.out.println(article);
        }
    }

}
