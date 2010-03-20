package no.java.portal.domain.service;

import fj.data.*;
import no.java.portal.domain.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.MembershipNo.*;
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
    private final UuidGenerator uuidGenerator;

    @Autowired
    public DefaultMemberService(MemberDao memberDao, MemberMailSender memberMailSender, UuidGenerator uuidGenerator) {
        this.memberDao = memberDao;
        this.memberMailSender = memberMailSender;
        this.uuidGenerator = uuidGenerator;
    }

    // -----------------------------------------------------------------------
    // MemberService Implementation
    // -----------------------------------------------------------------------

    public Member addMember(Member member) throws IOException {
        member = member.setId(membershipNo((int) memberDao.insert(member)));

        memberMailSender.sendNewAccountEmail(member);

        return member;
    }

    public void updateMember(Member member) {
        throw new RuntimeException("Not implemented");
    }

    public void addMailAddress(MembershipNo membershipNo, MailAddress mailAddress) {
        throw new RuntimeException("Not implemented");
    }

    public void resetPasswordRequest(MembershipNo no) throws UserNotFoundException {
        Option<Member> memberOption = memberDao.select(no);

        if (memberOption.isNone()) {
            throw new UserNotFoundException("No such user " + no);
        }

//        Member member = memberOption.some().resetPassword(uuidGenerator);
//
//        memberDao.update(member);
//
//        memberMailSender.sendResetPasswordEmail(member);
        throw new RuntimeException("Not implemented");
    }

    public void resetPassword(MembershipNo no, UUID uuid) {
        throw new RuntimeException("Not implemented");
    }

    public Member getMember(MembershipNo membershipNo) {
        return memberDao.select(membershipNo).some();
    }
}
