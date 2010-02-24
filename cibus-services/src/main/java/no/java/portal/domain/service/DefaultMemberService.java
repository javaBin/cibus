package no.java.portal.domain.service;

import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.MembershipNo.membershipId;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
@Transactional
public class DefaultMemberService implements MemberService {

    private final MemberDao memberDao;
    private final MemberMailSender memberMailSender;

    @Autowired
    public DefaultMemberService(MemberDao memberDao, MemberMailSender memberMailSender) {
        this.memberDao = memberDao;
        this.memberMailSender = memberMailSender;
    }

    // -----------------------------------------------------------------------
    // MemberService Implementation
    // -----------------------------------------------------------------------

    public Member addMember(Member member) throws IOException {
        member = member.setId(membershipId((int)memberDao.insert(member)));

        memberMailSender.sendNewAccountEmail(member);

        return member;
    }

    public void updateMember(Member member) {
        throw new RuntimeException("Not implemented");
    }

    public void addEmail(MembershipNo membershipNo, EmailAddress emailAddress) {
        throw new RuntimeException("Not implemented");
    }

    public void resetPasswordRequest(MembershipNo no) {
        Member member = memberDao.select(no);

        

        memberMailSender.sendResetPasswordEmail(member);
    }

    public void resetPassword(MembershipNo no, UUID uuid) {
        throw new RuntimeException("Not implemented");
    }
}
