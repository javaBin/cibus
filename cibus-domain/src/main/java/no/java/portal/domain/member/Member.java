package no.java.portal.domain.member;

import fj.*;
import fj.data.*;
import fj.data.List;
import static fj.data.List.single;
import static fj.data.Option.some;
import static java.lang.String.valueOf;
import no.java.portal.domain.*;
import org.joda.time.*;

import java.sql.*;
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
    public final Option<UUID> resetPasswordUuid;
    public final List<EmailAddress> emails;

    private Member(MembershipNo no, String firstName, String lastName, DateTime created, Option<DateTime> lastUpdated,
                   Option<UUID> resetPasswordUuid, List<EmailAddress> emails) {
        this.no = no;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdated = lastUpdated;
        this.resetPasswordUuid = resetPasswordUuid;
        this.emails = emails;
        this.created = created;
    }

    public Member setId(MembershipNo membershipNo) {
        return new Member(membershipNo, firstName, lastName, created, lastUpdated, resetPasswordUuid, emails);
    }

    public Member resetPassword(UuidGenerator uuidGenerator) {
        return new Member(no, firstName, lastName, created, lastUpdated, some(uuidGenerator.get()), emails);
    }

    public EmailAddress getPrimaryEmail() {
        return emails.head();
    }

    /**
     * Creates a 'new' member with membership no = 0.
     */
    public static Member createNewMember(String firstName, String lastName, EmailAddress emailAddress) {
        return new Member(MembershipNo.membershipId(0), firstName, lastName, new DateTime(), null, null, single(emailAddress));
    }

    public static Member fromResultSet(ResultSet rs, List<EmailAddress> emailAddresses) throws SQLException {
        return new Member(
                MembershipNo.membershipId(rs.getInt("membership_id")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                new DateTime(rs.getTimestamp("created").getTime()),
                rs.getTimestamp("last_updated") != null ? some(new DateTime(rs.getTimestamp("last_updated").getTime())) : Option.<DateTime>none(),
                rs.getString("reset_password_uuid") != null ? some(UUID.fromString(rs.getString("reset_password_uuid"))) : Option.<UUID>none(),
                emailAddresses);
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public static class MembershipNo {
        private final int value;

        private MembershipNo(int value) {
            this.value = value;
        }

        public static MembershipNo membershipId(int value) {
            return new MembershipNo(value);
        }

        @Override
        public String toString() {
            return valueOf(value);
        }
    }

    public static class EmailAddress {
        private final String value;

        private EmailAddress(String value) {
            this.value = value;
        }

        public static EmailAddress emailAddress(String value) {
            return new EmailAddress(value);
        }

        @Override
        public String toString() {
            return value;
        }

        public static F<String, EmailAddress> emailAddress = new F<String, EmailAddress>() {
            public EmailAddress f(String s) {
                return new EmailAddress(s);
            }
        };
    }
}
