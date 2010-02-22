package no.java.portal.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 4, 2009
 */
@Repository
public class Categories {

	private final SimpleJdbcTemplate template;

	@Autowired
	public Categories(DataSource dataSource) {
		template = new SimpleJdbcTemplate(dataSource);
	}

	public List<SubscribedCategory> getSubscribable() {
		ParameterizedRowMapper<SubscribedCategory> rowMapper = new ParameterizedRowMapper<SubscribedCategory>() {
			public SubscribedCategory mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return new SubscribedCategory(rs.getInt("content_id"), rs
						.getString("text_value"), false);
			}
		};
		String query = "select * from on_text " + "where field_id = 4 and "
				+ "content_id in (26, 35, 39, 260, 261, 778, 806)";
		List<SubscribedCategory> results = template.query(query, rowMapper);
		Collections.sort(results, new Comparator<SubscribedCategory>() {

			public int compare(SubscribedCategory o1, SubscribedCategory o2) {
				return o1.getCategoryId() - o2.getCategoryId();
			}

		});
		return results;
	}

}
