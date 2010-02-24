package no.java.portal.domain.member;

import fj.*;
import fj.Function;
import fj.data.List;
import static fj.data.List.*;
import fj.data.*;
import fj.pre.*;
import static java.lang.String.*;
import org.joda.time.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class Member {
    public final MembershipNo no;
    public final String firstName;
    public final String lastName;
    public final DateTime created;
    public final Option<DateTime> lastUpdated;
    public final List<MailAddress> mailAddresses;

    private Member(MembershipNo no,
                   String firstName, String lastName,
                   DateTime created, Option<DateTime> lastUpdated,
                   List<MailAddress> mailAddresses) {
        this.no = no;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdated = lastUpdated;
        this.mailAddresses = mailAddresses;
        this.created = created;
    }

    public Member setId(MembershipNo membershipNo) {
        return new Member(membershipNo, firstName, lastName, created, lastUpdated, mailAddresses);
    }

    public MailAddress getPrimaryMailAddress() {
        return mailAddresses.head();
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    public Collection<MailAddress> getMailAddressesAsCollection() {
        return mailAddresses.toCollection();
    }

    /**
     * Creates a 'new' member with membership no = 0.
     */
    public static Member createNewMember(String firstName, String lastName, MailAddress mailAddress) {
        return new Member(MembershipNo.membershipNo(0), firstName, lastName, new DateTime(), Option.<DateTime>none(), single(mailAddress));
    }

    public static Member fromDatabase(MembershipNo membershipNo, String firstName, String lastName,
                                      DateTime created, Option<DateTime> lastUpdated,
                                      List<MailAddress> mailAddresses) {
        return new Member(
                membershipNo,
                firstName, lastName,
                created, lastUpdated,
                mailAddresses);
    }

    public static final F<Member, MembershipNo> membershipNo = new F<Member, MembershipNo>() {
        public MembershipNo f(Member member) {
            return member.no;
        }
    };

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public static class MembershipNo {
        private final int value;

        private MembershipNo(int value) {
            this.value = value;
        }

        public static MembershipNo membershipNo(int value) {
            return new MembershipNo(value);
        }

        @Override
        public String toString() {
            return valueOf(value);
        }

        public int toInteger() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MembershipNo)) return false;

            MembershipNo that = (MembershipNo) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }

    public static class MailAddress {
        private final String value;

        private MailAddress(String value) {
            this.value = value;
        }

        public static MailAddress mailAddress(String value) {
            return new MailAddress(value);
        }

        @Override
        public String toString() {
            return value;
        }

        public static final F<String, MailAddress> mailAddress = new F<String, MailAddress>() {
            public MailAddress f(String s) {
                return new MailAddress(s);
            }
        };

        public static final Equal<MailAddress> equal = Equal.equal(Function.curry(new F2<MailAddress, MailAddress, Boolean>() {
            public Boolean f(MailAddress a, MailAddress b) {
                return a.value.equals(b.value);
            }
        }));
    }
}
