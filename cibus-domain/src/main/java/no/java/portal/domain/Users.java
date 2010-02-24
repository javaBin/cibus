package no.java.portal.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 24, 2009
 */
@Repository
public class Users {

    private static final String NEWS_MAP = "news_map";
    private static final String USERKEY = "userkey";
    private static final String EMAIL = "email";
    private SimpleJdbcTemplate template;
    private Categories categories;

    @Autowired
    public Users(@Qualifier("onpDataSource") DataSource dataSource, Categories categories) {
        template = new SimpleJdbcTemplate(dataSource);
        this.categories = categories;
    }

    public User getUser(String userName) throws UserNotFoundException {
        List<User> users = template.query("select * from on_user where username = ?", new UserRowMapper(true),
                new Object[] { userName });
        if (users.size() > 1) {
            throw new RuntimeException("One user " + userName + " expected; got " + users.size());
        } else if (users.size() == 0) {
            throw new UserNotFoundException("User " + userName + " not found");
        }
        return users.get(0);
    }

    public List<User> getAll() {
        return template.query("select * from on_user", new UserRowMapper(false), new Object[0]);
    }

    public void update(User user) {
        Properties props = createProperties(user);
        String sql = "update on_user set props = ? where id = ?";
        template.update(sql, encodePropertiesField(props), user.getId());
    }

    public void create(User user) {
        if (user.getId() != -1) {
            throw new RuntimeException("User already have id != -1");
        }
        String sql = "insert into on_user (password, props, username) values (?, ?, ?)";
        Properties props = createProperties(user);
        template.update(sql, user.getPassword(), encodePropertiesField(props), user.getUserName());
    }
    
    private Properties createProperties(User user) {
        Properties props = new Properties();
        if (user.getEmail() != null) {
            props.put(EMAIL, user.getEmail());
        }
        if (user.getUserkey() != null) {
            props.put(USERKEY, user.getUserkey());
        }
        HashMap<String, Boolean> newsMap = new HashMap<String, Boolean>();
        for (SubscribedCategory sc : user.getSubscribedCategories()) {
            if (sc.isSelected()) {
                newsMap.put(Integer.toString(sc.getCategoryId()), Boolean.TRUE);
            }
        }
        props.put(NEWS_MAP, newsMap);
        return props;
    }
    
    /**
     * @param props
     *            if null
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SubscribedCategory> createCategories(final Properties props) {
        Map<String, Boolean> newsMap = null;
        if (props != null) {
            newsMap = (Map<String, Boolean>) props.get(NEWS_MAP);
        }
        if (newsMap == null) {
            newsMap = new HashMap<String, Boolean>();
        }
        List<SubscribedCategory> cats = new ArrayList<SubscribedCategory>();
        for (SubscribedCategory template : categories.getSubscribable()) {
            int catId = template.getCategoryId();
            cats.add(new SubscribedCategory(catId, template.getDescription(), newsMap.get(Integer.toString(catId)) != null));
        }
        return cats;
    }

    final class UserRowMapper implements ParameterizedRowMapper<User> {
        final private boolean pwd;

        public UserRowMapper(boolean pwd) {
            this.pwd = pwd;
        }

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            String propsField = rs.getString("props");
            Properties props = decodePropertiesField(propsField);
            String password = pwd ? rs.getString("password") : null;
            String email = (String) props.get(EMAIL);
            String userkey = (String) props.get(USERKEY);
            List<SubscribedCategory> cats = createCategories(props);
            return new User(rs.getLong("id"), rs.getString("username"), password, cats, email, userkey);
        }

    }

    static protected Properties decodePropertiesField(String propsField) {
        if (propsField == null) {
            return new Properties();
        }
        byte[] bs = Base64.decodeBase64(propsField.getBytes());
        return (Properties) SerializationUtils.deserialize(bs);
    }

    static protected String encodePropertiesField(Properties props) {
        byte[] serialised = SerializationUtils.serialize(props);
        return new String(Base64.encodeBase64(serialised));
    }

}
