package no.java.portal.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Repository(value = "memberPeople")
public class MemberPeopleImpl implements MemberPeople {

    private final SimpleJdbcTemplate template;
    private final MemberCompanies memberCompanies;

    @Autowired
    public MemberPeopleImpl(@Qualifier("cibusDataSource") DataSource dataSource,
                            MemberCompanies memberCompanies) {
        this.memberCompanies = memberCompanies;
        template = new SimpleJdbcTemplate(dataSource);
    }

    public List<MemberPerson> getCurrentMemberPeople() {
        return template.query(MemberPeopleRowMapper.MEMBERS_SQL, new MemberPeopleRowMapper(), new Date());
    }

    public MemberPerson findByEMailAndPassword(String email, String password) {
        MemberPerson memberPerson = findByEMail(email);
        if (memberPerson == null || !password.equals(memberPerson.getPassword())) {
            return null;
        }
        return memberPerson;
    }

    public MemberPerson findByEMail(String email) {
        List<MemberPerson> list = template.query(MemberPeopleRowMapper.USER_NAME_SQL, new MemberPeopleRowMapper(), email);
        if (list.size() > 1) {
            throw new RuntimeException("Multiple users match " + list.size());
        } else if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public void save(MemberPerson memberPerson) {
        System.out.println("Member person: " + memberPerson);
    }

    class MemberPeopleRowMapper implements RowMapper<MemberPerson> {
        static final String BASE_SQL = "select mp.id, mp.first_name, mp.last_name, mp.password, mp.phone_number, mp.address, mp.email, mp.member_company_id " +
                "from jb_member_people mp join jb_memberships ms on (mp.id = ms.member_person_id or mp.member_company_id = ms.member_company_id) ";
        static final String MEMBERS_SQL = BASE_SQL + " and ms.valid_to > ? ";
        static final String USER_NAME_SQL = BASE_SQL + " where email = ?";

        public MemberPerson mapRow(ResultSet rs, int rowNum) throws SQLException {
            int personId = rs.getInt("id");
            Long memberCompanyId = (Long) rs.getObject("member_company_id");
            MemberCompany memberCompany = null;
            if (memberCompanyId != null) {
                memberCompany = memberCompanies.find(memberCompanyId);
            }
            return new MemberPerson(personId, rs.getString("first_name"), rs.getString("last_name"), rs.getString("password"), rs.getString("phone_number"), rs.getString("address"), rs.getString("email"), memberCompany);
        }
    }

}
