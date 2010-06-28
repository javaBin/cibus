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
    public MemberCompaniesImpl(@Qualifier("cibusDataSource") DataSource dataSource) {
        this.template = new SimpleJdbcTemplate(dataSource);
    }

    public Map<Integer, MemberCompany> getMemberCompanyMap() {
        List<MemberCompany> list = template.query(MemberCompanyRowMapper.VALID_COMPANIES_SQL, new MemberCompanyRowMapper(), new Date());
        Map<Integer, MemberCompany> companyMap = new HashMap<Integer, MemberCompany>();
        for (MemberCompany memberCompany : list) {
            companyMap.put(memberCompany.getId(), memberCompany);
        }
        return companyMap;
    }

    public MemberCompany find(long id) {
        List<MemberCompany> list = template.query(MemberCompanyRowMapper.COMPANY_SQL, new MemberCompanyRowMapper(), id);
        if (list.size() > 1) {
            throw new RuntimeException("Multiple companies (" + list.size() + ") match id " + id);
        } else if (list.size() == 0) {
            throw new RuntimeException("No companies found for id " + id);
        }
        return list.get(0);
    }

    static class MemberCompanyRowMapper implements RowMapper<MemberCompany> {
        private static final String BASE_SQL = "select * " +
                "from jb_member_companies mc, jb_memberships ms " +
                "where mc.id = ms.member_company_id ";
        static String VALID_COMPANIES_SQL = BASE_SQL + " and ms.valid_to > ?";
        static String COMPANY_SQL = BASE_SQL + " and ms.id = ?";

        public MemberCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberCompany(rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getInt("contact_member_person_id"));
        }
    }

}
