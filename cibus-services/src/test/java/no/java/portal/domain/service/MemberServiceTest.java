package no.java.portal.domain.service;

import com.icegreen.greenmail.util.GreenMail;
import no.java.portal.domain.member.Member;
import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static no.java.portal.domain.member.Member.MailAddress.mailAddress;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring.xml", "classpath:/test-cibus-services-spring.xml"})
@Tags("dev")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GreenMail greenMail;

    @Test
    public void testBasic() throws IOException, MessagingException {
        Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);

        logger.info("yo");

        Member member = Member.createNewMember("Trygve", "Laugstol", mailAddress("foo@bar.com"));

        //Member savedMember = 
        memberService.addMember(member);

        MimeMessage[] mimeMessages = greenMail.getReceivedMessages();
        assertEquals(1, mimeMessages.length);
        assertEquals(1, mimeMessages[0].getHeader("To").length);
        assertEquals("foo@bar.com", mimeMessages[0].getHeader("To")[0]);
    }
}
