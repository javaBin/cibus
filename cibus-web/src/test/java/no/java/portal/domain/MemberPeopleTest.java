package no.java.portal.domain;

import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Tags("dev")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring.xml" })
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
