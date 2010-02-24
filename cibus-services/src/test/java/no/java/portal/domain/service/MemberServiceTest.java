package no.java.portal.domain.service;

import com.icegreen.greenmail.util.*;
import static junit.framework.Assert.*;
import no.java.portal.domain.member.*;
import static no.java.portal.domain.member.Member.MailAddress.*;
import org.constretto.test.*;
import org.junit.*;
import org.junit.runner.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring.xml", "classpath:/test-cibus-services-spring.xml"})
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

        Member savedMember = memberService.addMember(member);

        MimeMessage[] mimeMessages = greenMail.getReceivedMessages();
        assertEquals(1, mimeMessages.length);
        assertEquals(1, mimeMessages[0].getHeader("To").length);
        assertEquals("foo@bar.com", mimeMessages[0].getHeader("To")[0]);
    }
}
