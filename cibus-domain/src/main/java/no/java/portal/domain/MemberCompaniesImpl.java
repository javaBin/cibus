package no.java.portal.domain;

import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Repository("memberCompanies")
public class MemberCompaniesImpl implements MemberCompanies {

    private final SimpleJdbcTemplate template;

    @Autowired
    public MemberCompaniesImpl(@Qualifier("onpDataSource") DataSource dataSource) {
        this.template = new SimpleJdbcTemplate(dataSource);
    }

    public Map<Integer, MemberCompany> getMemberCompanyMap() {
        String sql = "select * " +
                "from jb_member_companies mc, jb_memberships ms " +
                "where mc.id = ms.member_company_id and ms.valid_to > ?";
        List<MemberCompany> list = template.query(sql, new ParameterizedRowMapper<MemberCompany>() {
            public MemberCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MemberCompany(rs.getInt("id"), rs.getString("name"), rs.getInt("contact_member_person_id"));
            }
        }, new Date());
        Map<Integer, MemberCompany> companyMap = new HashMap<Integer, MemberCompany>();
        for (MemberCompany memberCompany : list) {
            companyMap.put(memberCompany.getId(), memberCompany);
        }
        return companyMap;
    }
}
