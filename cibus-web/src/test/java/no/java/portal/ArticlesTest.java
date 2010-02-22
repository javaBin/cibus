package no.java.portal;

import no.java.portal.domain.Articles;

import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring.xml"})
public class ArticlesTest {

    @Autowired
    Articles articles;

    @Test
    public void foo() {
//        List<NewsItem> newsItems = this.articles.getActiveArticleByCategory(Category.nyheter, 0, 10);
//        assertNotNull(newsItems);
//
//        for (NewsItem article : newsItems) {
//            System.out.println(article);
//        }
    }
}
