package no.java.portal.domain.service;

import org.apache.commons.io.IOUtils;
import org.constretto.annotation.Tags;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
@Service
public class InmemoryDatabase implements InitializingBean {

    @SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
    @Tags
    private List<String> tags;

    private final JdbcTemplate template;

    @Autowired
    public InmemoryDatabase(DataSource cibusDataSource) {
        template = new JdbcTemplate(cibusDataSource);
    }

    public void afterPropertiesSet() throws Exception {
        if (tags.contains("dev")) {
            String content = IOUtils.toString(getClass().getResourceAsStream("/sql/cibus.sql"));
            String[] sqls = content.split(";");
            for (String sql : sqls) {
                sql = sql.trim();
                if (!sql.matches(".*alter\\s+sequence\\s+.*\\s+owned\\s+by.*")) {
                    sql = sql.replaceAll("integer", "bigint");
                    template.update(sql);
                }
            }
        }
    }

}
