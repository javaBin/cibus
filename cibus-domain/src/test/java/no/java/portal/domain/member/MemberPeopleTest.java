package no.java.portal.domain.member;

import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Tags("local")
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-domain-test-module.xml"})
public class MemberPeopleTest {

    @Autowired
    private MemberPeople memberPeople;

    @Test
    public void test() {
        List<MemberPerson> list = memberPeople.getCurrentMemberPeople();
        for (MemberPerson memberPerson : list) {
            System.out.println("Person: " + memberPerson);
        }
        assertEquals(3, list.size());
    }

}
