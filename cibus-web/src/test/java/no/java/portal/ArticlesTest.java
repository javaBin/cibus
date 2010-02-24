package no.java.portal;

import no.java.portal.domain.*;
import org.constretto.annotation.*;
import org.constretto.test.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;

@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
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
