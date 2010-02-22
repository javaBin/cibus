package no.java.portal.infrastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import no.java.portal.domain.Categories;
import no.java.portal.domain.SubscribedCategory;
import no.java.portal.domain.User;
import no.java.portal.domain.UserNotFoundException;
import no.java.portal.domain.Users;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 17, 2009
 */
@Component
@Path("/user")
public class UserResource {

    private static final Log logger = LogFactory.getLog(UserResource.class);

    private Categories categories;

    private Users users;

    @Autowired
    public UserResource(Categories categories, Users users) {
        super();
        this.categories = categories;
        this.users = users;
    }

    @POST
    public void updateUser(MultivaluedMap<String, String> selectedCategories) {
        final User user;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            user = users.getUser(auth.getName());
        } catch (UserNotFoundException e) {
            WebApplicationException ex = new WebApplicationException(e, Response.Status.UNAUTHORIZED);
            logger.error("User " + auth.getName() + " does not exist", ex);
            throw ex;
        }
        final User newUser = createUserObject(user, categories, selectedCategories);
        users.update(newUser);
    }

    static User createUserObject(User user, Categories categories, MultivaluedMap<String, String> selectedCategories) {
        String userName = user.getUserName();
        String email = user.getEmail();
        String password = user.getPassword();
        Set<Integer> categoryIds = new HashSet<Integer>();
        for (Entry<String, List<String>> entry : selectedCategories.entrySet()) {
            String key = entry.getKey();
            if (key.equals("userName")) {
                userName = entry.getValue().iterator().next();
            } else if (key.equals("password")) {
                password = entry.getValue().iterator().next();
            } else if (key.equals("confirmPassword")) {
                // Ignore (checked by javascript)
            } else if (key.equals("email")) {
                email = entry.getValue().iterator().next();
            } else if (key.startsWith("category.")) {
                categoryIds.add(Integer.valueOf(key.substring(key.indexOf('.') + 1)));
            } else {
                WebApplicationException ex = new WebApplicationException(Response.Status.BAD_REQUEST);
                logger.error("Unknown form entry " + key, ex);
                throw ex;
            }
        }
        List<SubscribedCategory> subscribedCategories = new ArrayList<SubscribedCategory>();
        for (SubscribedCategory sc : categories.getSubscribable()) {
            subscribedCategories.add(new SubscribedCategory(sc.getCategoryId(), sc.getDescription(), categoryIds.contains(sc
                    .getCategoryId())));
        }
        User newUser = new User(user.getId(), userName, password, subscribedCategories, email, user.getUserkey());
        return newUser;
    }

}
