package no.java.portal.domain.service;

import fj.*;
import fj.data.*;
import fj.data.HashMap;
import fj.data.List;
import fj.pre.*;
import static junit.framework.Assert.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.EmailAddress.*;
import static no.java.portal.domain.member.Member.MembershipNo.*;
import no.java.portal.domain.service.AuthenticationService.*;
import org.constretto.test.*;
import org.joda.time.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.test.context.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring.xml", "classpath:/test-cibus-services-spring.xml"})
public class AuthenticationServiceTest {

    @Test
    public void testBasic() {
        final HashMap<MembershipNo, Member> members = new HashMap<MembershipNo, Member>(Equal.<MembershipNo>anyEqual(), Hash.<MembershipNo>anyHash());
        final HashMap<MembershipNo, String> passwords = new HashMap<MembershipNo, String>(Equal.<MembershipNo>anyEqual(), Hash.<MembershipNo>anyHash());

        EmailAddress fooAtBarCom = emailAddress("foo@bar.com");
        EmailAddress failAtBarCom = emailAddress("fail@bar.com");

        MembershipNo membershipNo = membershipNo(1);
        members.set(membershipNo, Member.fromDatabase(membershipNo, "first", "last", new DateTime(), Option.<DateTime>none(), List.single(fooAtBarCom)));
        passwords.set(membershipNo, "password");

        AuthenticationService authenticationService = new AuthenticationService(new HashMapMemberDao(members, passwords));
        CibusAuthentication cibusAuthentication;

        cibusAuthentication = authenticationService.isAuthenticated(failAtBarCom, "blah");
        assertTrue(cibusAuthentication.member.isNone());

        cibusAuthentication = authenticationService.isAuthenticated(fooAtBarCom, "blah");
        assertTrue(cibusAuthentication.member.isNone());

        cibusAuthentication = authenticationService.isAuthenticated(fooAtBarCom, "password");
        System.out.println("cibusAuthentication = " + cibusAuthentication);
        assertTrue(cibusAuthentication.member.isSome());
        assertEquals("first", cibusAuthentication.member.some().firstName);
    }

    private static class HashMapMemberDao implements MemberDao {
        private final HashMap<MembershipNo, Member> members;
        private final HashMap<MembershipNo, String> passwords;

        private HashMapMemberDao(HashMap<MembershipNo, Member> members, HashMap<MembershipNo, String> passwords) {
            this.members = members;
            this.passwords = passwords;
        }

        public long insert(Member member) {
            throw new RuntimeException("Not implemented");
        }

        public void update(Member member) {
            throw new RuntimeException("Not implemented");
        }

        public Option<Member> select(MembershipNo membershipNo) {
            return members.get(membershipNo);
        }

        public Option<MembershipNo> findMemberByEmail(final EmailAddress emailAddress) {
            return members.values().find(new F<Member, Boolean>() {
                public Boolean f(Member member) {
                    return member.emails.find(EmailAddress.equal.eq(emailAddress)).isSome();
                }
            }).map(Member.membershipNo);
        }

        public boolean checkPassword(MembershipNo membershipNo, String password) {
            return passwords.get(membershipNo).exists(Equal.<String>anyEqual().eq(password));
        }
    }
}
