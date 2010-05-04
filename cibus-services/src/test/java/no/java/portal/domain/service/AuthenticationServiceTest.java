package no.java.portal.domain.service;

import fj.F;
import fj.data.HashMap;
import fj.data.List;
import fj.data.Option;
import fj.pre.Equal;
import fj.pre.Hash;
import no.java.portal.domain.member.Member;
import no.java.portal.domain.member.Member.MailAddress;
import no.java.portal.domain.member.Member.MembershipNo;
import no.java.portal.domain.service.AuthenticationService.CibusAuthentication;
import org.constretto.annotation.Tags;
import org.constretto.test.ConstrettoSpringJUnit4ClassRunner;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static no.java.portal.domain.member.Member.MailAddress.mailAddress;
import static no.java.portal.domain.member.Member.MembershipNo.membershipNo;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@RunWith(ConstrettoSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-services-test-module.xml"})
@Tags("local")
public class AuthenticationServiceTest {
                
    @Test
    public void testBasic() {
        final HashMap<MembershipNo, Member> members = new HashMap<MembershipNo, Member>(Equal.<MembershipNo>anyEqual(), Hash.<MembershipNo>anyHash());
        final HashMap<MembershipNo, String> passwords = new HashMap<MembershipNo, String>(Equal.<MembershipNo>anyEqual(), Hash.<MembershipNo>anyHash());

        MailAddress fooAtBarCom = mailAddress("foo@bar.com");
        MailAddress failAtBarCom = mailAddress("fail@bar.com");

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

        public Option<MembershipNo> findMemberByMailAddress(final MailAddress mailAddress) {
            return members.values().find(new F<Member, Boolean>() {
                public Boolean f(Member member) {
                    return member.mailAddresses.find(MailAddress.equal.eq(mailAddress)).isSome();
                }
            }).map(Member.membershipNo);
        }

        public boolean checkPassword(MembershipNo membershipNo, String password) {
            return passwords.get(membershipNo).exists(Equal.<String>anyEqual().eq(password));
        }
    }
}
