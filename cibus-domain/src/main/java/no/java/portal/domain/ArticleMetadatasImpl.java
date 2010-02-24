package no.java.portal.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(value = "articleMetadatas")
public class ArticleMetadatasImpl implements ArticleMetadatas {

	protected final static String FIELDS = "a.id, a.created, a.modified";
	protected final SimpleJdbcTemplate template;

	@Autowired
	protected ArticleMetadatasImpl(@Qualifier("onpDataSource") DataSource dataSource) {
		template = new SimpleJdbcTemplate(dataSource);
	}

	public List<ArticleMetadata> getActiveArticleIdsByCategory(
			List<Category> categories, int offset, int limit) {
	    String categoryIdHolders = String.format("%" + categories.size() + "s", "").replaceAll(" ", ",?").substring(1);
		String article = "select "
				+ FIELDS
				+ " from jb_active_articles a,on_contentrelation "
				+ "where id = relatee and relater in (" + categoryIdHolders + ") "
				+ "order by modified desc offset ? limit ?";

		List<Object> argList = new ArrayList<Object>();
		for (Category c : categories) {
		    argList.add(c.getId());
		}
		argList.addAll(Arrays.asList(offset, limit));
		// noinspection unchecked
		return template.query(article, new ArticleMetadataMapper(), argList.toArray());
	}

	public List<ArticleMetadata> getActiveArticleIds(Category category, DateTime from, DateTime to) {
		String article = "select "
				+ FIELDS
				+ " from jb_active_articles a,on_contentrelation "
				+ "where id = relatee and relater=? and modified between ? and ? "
				+ "order by modified desc";
		Object[] args = new Object[] { category.getId(), new java.sql.Date(from.toInstant().getMillis()), new java.sql.Date(to.toInstant().getMillis()) };
		// noinspection unchecked
		return template.query(article, new ArticleMetadataMapper(), args);
	}

	public ArticleMetadata getArticle(int id) {
		return template.queryForObject("select " + FIELDS
				+ " from jb_active_articles a where id=?",
				new ArticleMetadataMapper(), id);
	}

	public List<DateTime> getYears() {
		String lastSQL = "select modified from jb_active_articles where modified = (select max(modified) from jb_active_articles) limit 1";
		DateTime last = new DateTime(template.queryForObject(lastSQL,
				Date.class, new Object[0]));
		String firstSQL = "select modified from jb_active_articles where modified = (select min(modified) from jb_active_articles) limit 1";
		DateTime first = new DateTime(template.queryForObject(firstSQL,
				Date.class, new Object[0]));
		return JodaTimeFunctions.getYears(first, last);
	}

	protected class ArticleMetadataMapper implements
			ParameterizedRowMapper<ArticleMetadata> {

		public ArticleMetadata mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			int id = rs.getInt(1);
			Timestamp created = rs.getTimestamp(2);
            Timestamp modified = rs.getTimestamp(3);
            return new ArticleMetadata(id, new LocalDateTime(created.getTime()), new LocalDateTime(modified.getTime()));
		}
	}

}
