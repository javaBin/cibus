package no.java.portal.domain.service;

import no.java.portal.domain.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface MemberService {
    Member addMember(Member member) throws IOException;

    void updateMember(Member member);

    void addMailAddress(MembershipNo membershipNo, MailAddress mailAddress);

//    void verifyEmail(MembershipNo no, MailAddress mailAddress, String code);
//
//    void mergeAccounts(MembershipNo membershipA, MembershipNo membershipB);

    void resetPasswordRequest(MembershipNo no) throws UserNotFoundException;

    void resetPassword(MembershipNo no, UUID uuid);

    Member getMember(MembershipNo membershipNo);
}
