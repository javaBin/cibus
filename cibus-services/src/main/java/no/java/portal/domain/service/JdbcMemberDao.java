package no.java.portal.domain.service;

import fj.*;
import fj.data.List;
import static fj.data.List.*;
import fj.data.*;
import static fj.data.Option.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.MembershipNo.membershipNo;
import org.joda.time.*;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.object.*;
import org.springframework.stereotype.*;

import javax.sql.*;
import java.sql.*;
import java.util.HashMap;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Repository
public class JdbcMemberDao implements InitializingBean, MemberDao {

    private final static String MEMBER_FIELDS = "membership_no, first_name, last_name, created, last_updated";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertMember;
    private final SqlUpdate updateMember;
    private final SimpleJdbcInsert insertEmail;
    private final SqlUpdate deleteEmailsForMemberNo;

    @Autowired
    public JdbcMemberDao(@Qualifier("cibusDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.insertMember = new SimpleJdbcInsert(dataSource).
                withTableName("member").
                usingGeneratedKeyColumns("membership_no");
        this.updateMember = new SqlUpdate(dataSource, "update member set first_name=?, last_name=? where membership_no=?");

        this.insertEmail = new SimpleJdbcInsert(dataSource).
                withTableName("email_address");
        this.deleteEmailsForMemberNo = new SqlUpdate(dataSource, "delete from email_address where membership_no=?");
    }

    public void afterPropertiesSet() throws Exception {
        insertMember.compile();
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public long insert(final Member member) {
        member.emails.zipIndex().foreach(new Effect<P2<EmailAddress, Integer>>() {
            public void e(final P2<EmailAddress, Integer> p2) {
                insertEmail.execute(new HashMap<String, Object>() {{
                    put("value", p2._1().toString());
                    put("index", p2._2());
                }});
            }
        });

        return (Long) insertMember.executeAndReturnKey(new java.util.HashMap<String, Object>(3) {{
            put("first_name", member.firstName);
            put("last_name", member.lastName);
            put("created", new Timestamp(System.currentTimeMillis()));
        }});
    }

    public void update(final Member member) {
        updateMember.update(member.firstName, member.lastName, member.no.toInteger());
    }

    public Option<Member> select(MembershipNo membershipNo) {
        List<EmailAddress> emailAddresses = iterableList(
                jdbcTemplate.queryForList("select value from email_address where membership_no=? order by index", String.class, membershipNo.toInteger())).
                map(EmailAddress.emailAddress);
        try {
            return some(jdbcTemplate.queryForObject("select " + MEMBER_FIELDS + " from member where membership_no=?", mapper.f(emailAddresses), membershipNo.toInteger()));
        } catch (IncorrectResultSizeDataAccessException e) {
            return none();
        }
    }

    public Option<MembershipNo> findMemberByEmail(EmailAddress emailAddress) {
        String sql = "select membership_no from email_address where value=?";

        try {
            return some(membershipNo(jdbcTemplate.queryForInt(sql, emailAddress.toString())));
        } catch (IncorrectResultSizeDataAccessException e) {
            return none();
        }
    }

    public boolean checkPassword(MembershipNo membershipNo, String password) {
        String sql = "select count(membership_no) from member where password=? and membership_no=?";

        return jdbcTemplate.queryForInt(sql, password, membershipNo.toInteger()) > 0;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    F<List<EmailAddress>, ParameterizedRowMapper<Member>> mapper = new F<List<EmailAddress>, ParameterizedRowMapper<Member>>() {
        public ParameterizedRowMapper<Member> f(final List<EmailAddress> emailAddresses) {
            return new ParameterizedRowMapper<Member>() {
                public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return Member.fromDatabase(
                            MembershipNo.membershipNo(rs.getInt("membership_no")),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            new DateTime(rs.getTimestamp("created").getTime()),
                            rs.getTimestamp("last_updated") != null ? some(new DateTime(rs.getTimestamp("last_updated").getTime())) : Option.<DateTime>none(),
                            emailAddresses);
                }
            };
        }
    };
}
