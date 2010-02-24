package no.java.portal.domain;

import org.constretto.annotation.*;
import org.constretto.test.*;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;

import java.util.*;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class MemberPeopleTest {

    @Autowired
    private MemberPeople memberPeople;

    @Test
    @Ignore
    public void test() {
        List<MemberPerson> list = memberPeople.getCurrentMemberPeople();
        for (MemberPerson memberPerson : list) {
            System.out.println("Person: " + memberPerson);
        }
        assertEquals(3, list.size());
    }

}
