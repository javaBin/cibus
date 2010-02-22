package no.java.portal.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Feb 24, 2009
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5850088618894141406L;

    final private long id;
    final private String userName;
    final private String password;
    final private List<SubscribedCategory> subscribedCategories;
    final private String email;
    final private String userkey;

    public User(String userName, String password, List<SubscribedCategory> subscribedCategories, String email,
            String userkey) {
        this(-1, userName, password, subscribedCategories, email, userkey);
    }

    public User(long id, String userName, String password, List<SubscribedCategory> subscribedCategories, String email,
            String userkey) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.subscribedCategories = subscribedCategories;
        this.email = email;
        this.userkey = userkey;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<SubscribedCategory> getSubscribedCategories() {
        return subscribedCategories;
    }

    public String getEmail() {
        return email;
    }

    public String getUserkey() {
        return userkey;
    }

    @Override
    public String toString() {
        return (new ReflectionToStringBuilder(this) {
            protected boolean accept(Field f) {
                return super.accept(f) && !f.getName().equals("password");
            }
        }).toString();
    }

}
