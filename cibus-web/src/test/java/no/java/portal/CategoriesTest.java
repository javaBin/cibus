package no.java.portal;

import no.java.portal.domain.*;
import org.constretto.annotation.*;
import org.constretto.test.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 4, 2009
 */
@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class CategoriesTest {

    @Autowired
    Categories categories;

    @Test
    public void test() {
        // List<CategoryDetails> all = new ArrayList<CategoryDetails>(categories
        // .getSubscribable());
        // Collections.sort(all, new Comparator<CategoryDetails>() {
        // public int compare(CategoryDetails o1, CategoryDetails o2) {
        // return o1.getCategoryId() - o2.getCategoryId();
        // }
        // });
        // System.err.println("Count: " + all.size());
        // for (CategoryDetails cat : all) {
        // System.err.println("Category id: " + cat.getCategoryId()
        // + ", description: " + cat.getDescription());
        // }
    }

}
