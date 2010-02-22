package no.java.portal.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Repository(value = "memberPeople")
public class MemberPeopleImpl implements MemberPeople {

    private final SimpleJdbcTemplate template;
    private final MemberCompanies memberCompanies;

    @Autowired
    public MemberPeopleImpl(DataSource dataSource, MemberCompanies memberCompanies) {
        this.memberCompanies = memberCompanies;
        template = new SimpleJdbcTemplate(dataSource);
    }

    public List<MemberPerson> getCurrentMemberPeople() {
        final Map<Integer, MemberCompany> companyMap = memberCompanies.getMemberCompanyMap();
        String sql = "select mp.id, mp.onp_id, mp.member_company_id, mp.name, mp.hidden " +
                "from jb_member_people mp join jb_memberships ms on (mp.id = ms.member_person_id or mp.member_company_id = ms.member_company_id) and ms.valid_to > ? " +
                "where mp.hidden = false";
        return template.query(sql, new ParameterizedRowMapper<MemberPerson>() {
            public MemberPerson mapRow(ResultSet rs, int rowNum) throws SQLException {
                int personId = rs.getInt("id");
                MemberCompany memberCompany = companyMap.get(rs.getInt("member_company_id"));
                return new MemberPerson(personId, (Integer) rs.getObject("onp_id"), memberCompany, rs.getString("name"), rs.getBoolean("hidden"));
            }
        }, new Date());
    }

}
