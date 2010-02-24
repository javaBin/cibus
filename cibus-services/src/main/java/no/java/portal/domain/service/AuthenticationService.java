package no.java.portal.domain.service;

import fj.data.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final MemberDao memberDao;

    @Autowired
    public AuthenticationService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public static class CibusAuthentication {
        public final Option<Member> member;

        public CibusAuthentication(Option<Member> member) {
            this.member = member;
        }
    }

    public static final CibusAuthentication failedAuthentication = new CibusAuthentication(Option.<Member>none());

    public CibusAuthentication isAuthenticated(MailAddress mail, String password) {
        Option<MembershipNo> membershipNoOption = memberDao.findMemberByMailAddress(mail);

        if (membershipNoOption.isNone()) {
            logger.info("Failed authentication for mail '" + mail + "': No such mail");

            return failedAuthentication;
        }

        MembershipNo membershipNo = membershipNoOption.some();
        boolean authenticated = memberDao.checkPassword(membershipNo, password);

        if (!authenticated) {
            logger.info("Failed authentication for mail '" + mail + "': invalid password");
            return failedAuthentication;
        }

        Member member = memberDao.select(membershipNo).some();

        logger.info("Authenticated '" + member.getDisplayName() + "' using mail address '" + mail + ".");

        return new CibusAuthentication(memberDao.select(membershipNo));
    }
}
