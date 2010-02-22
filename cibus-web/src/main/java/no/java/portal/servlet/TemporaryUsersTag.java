package no.java.portal.servlet;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import no.java.portal.domain.User;
import no.java.portal.domain.Users;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Apr 21, 2009
 */
public class TemporaryUsersTag extends CibusSimpleTagSupport {

    protected String userkey;
    protected String var;

    @Override
    public void doTag() throws JspException, IOException {
        Ehcache temporaryUserCache = getBean("temporaryUserCache", Ehcache.class);
        Element elem = temporaryUserCache.get(userkey);
        User user;
        if (elem == null) {
            user = null;
        } else {
            user = (User) elem.getValue();
        }
        if (user != null) {
            Users users = getBean("users", Users.class);
            users.create(user);
            temporaryUserCache.remove(userkey);
            temporaryUserCache.remove(user.getUserName());
        }
        getJspContext().setAttribute(var, user);
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setVar(String var) {
        this.var = var;
    }

}
