package no.java.portal.domain;

import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Tags("local")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class UsersTest {

    @Autowired
    Users users;

    @Test
    @Ignore
    public void test() throws Exception {
        List<SubscribedCategory> cats = users.getUser("thoraage").getSubscribedCategories();
        for (SubscribedCategory cat : cats) {
            System.out.println("Cat id: " + cat.getCategoryId() + ", name: "
                    + cat.getDescription() + ", sel: " + cat.isSelected());
        }
    }

    @Test
    @Ignore
    public void test2() {
        List<User> all = users.getAll();
        Set<String> keys = new HashSet<String>();
        Set<String> suspicious = new HashSet<String>();
        for (User user : all) {
            /*Properties props = user.getProps();
            if (props != null) {
                for (Object key : props.keySet()) {
                    if (key.equals("mail")) {
                        suspicious.add(user.getUserName());
                    }
                    keys.add((String) key);
                }
            }*/
        }
        System.out.println("Keys: " + keys);
        System.out.println("Suspicious people: " + suspicious);
    }

    @Test
    @Ignore
    public void testPropertiesEncoding() {
        Properties props = new Properties();
        props.put("Hei", "Hopp");
        String encoded = Users.encodePropertiesField(props);
        assertEquals("Hopp", Users.decodePropertiesField(encoded).get("Hei"));
    }
}
