package no.java.portal.domain.service;

import fj.*;
import fj.data.*;
import static fj.data.List.*;
import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.MembershipNo.membershipId;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.stereotype.*;

import javax.sql.*;
import java.sql.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Repository
public class MemberDao implements InitializingBean {

    private final static String MEMBER_FIELDS = "membership_id, first_name, last_name";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertMember;

    @Autowired
    public MemberDao(@Qualifier("cibusDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.insertMember = new SimpleJdbcInsert(dataSource).
                withTableName("member").
                usingGeneratedKeyColumns("membership_no");
    }

    public void afterPropertiesSet() throws Exception {
        insertMember.compile();
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public long insert(final Member member) {
        return (Long) insertMember.executeAndReturnKey(new java.util.HashMap<String, Object>(3) {{
            put("first_name", member.firstName);
            put("last_name", member.lastName);
            put("created", new Timestamp(System.currentTimeMillis()));
        }});
    }

    public Member select(MembershipNo membershipNo) {
        List<EmailAddress> emailAddresses = iterableList(
                jdbcTemplate.queryForList("select email_address where membership_id=?", String.class, membershipNo.toString())).
                map(EmailAddress.emailAddress);
        return jdbcTemplate.queryForObject("select " + MEMBER_FIELDS + " from member where membership_id=?", mapper.f(emailAddresses), membershipNo.toString());
    }

    public MembershipNo findMemberByEmail(EmailAddress emailAddress) {
        String sql = "select membership_no from email_address where value=?";

        return membershipId(jdbcTemplate.queryForInt(sql, emailAddress));
    }

    public boolean checkPassword(MembershipNo membershipNo, String password) {
        String sql = "select count(membership_no) from member where password=? and membership_no=?";

        return jdbcTemplate.queryForInt(sql, password, membershipNo.toString()) > 0;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    F<List<EmailAddress>, ParameterizedRowMapper<Member>> mapper = new F<List<EmailAddress>, ParameterizedRowMapper<Member>>() {
        public ParameterizedRowMapper<Member> f(final List<EmailAddress> emailAddresses) {
            return new ParameterizedRowMapper<Member>() {
                public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return Member.fromResultSet(rs, emailAddresses);
                }
            };
        }
    };
}
