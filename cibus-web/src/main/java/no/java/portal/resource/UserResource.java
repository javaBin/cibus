package no.java.portal.resource;

import no.java.portal.domain.*;
import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.Map.*;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 17, 2009
 */
@Component
@Path("/user")
public class UserResource {

    private static final Log logger = LogFactory.getLog(UserResource.class);

    private final Categories categories;

    private final Users users;

    @Autowired
    public UserResource(Categories categories, Users users) {
        this.categories = categories;
        this.users = users;
    }

    @POST
    @Consumes()
    public void updateUser(MultivaluedMap<String, String> selectedCategories) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            User user = users.getUser(auth.getName());
            user = createUserObject(user, categories, selectedCategories);
            users.update(user);
        } catch (UserNotFoundException e) {
            WebApplicationException ex = new WebApplicationException(e, Response.Status.UNAUTHORIZED);
            logger.error("User " + auth.getName() + " does not exist", ex);
            throw ex;
        }
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
        return new User(user.getId(), userName, password, subscribedCategories, email, user.getUserkey());
    }
}
