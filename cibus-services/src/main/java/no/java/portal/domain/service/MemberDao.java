package no.java.portal.domain.service;

import fj.data.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface MemberDao {
    long insert(Member member);

    void update(Member member);

    Option<Member> select(MembershipNo membershipNo);

    Option<MembershipNo> findMemberByMailAddress(MailAddress mailAddress);

    boolean checkPassword(MembershipNo membershipNo, String password);
}
