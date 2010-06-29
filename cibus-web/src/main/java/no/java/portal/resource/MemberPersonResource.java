package no.java.portal.resource;

import no.java.portal.domain.Categories;
import no.java.portal.domain.SubscribedCategory;
import no.java.portal.domain.User;
import no.java.portal.domain.member.MemberPeople;
import no.java.portal.domain.member.MemberPerson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 17, 2009
 */
@Component
@Path("/memberPeople")
public class MemberPersonResource {

    private static final Log logger = LogFactory.getLog(MemberPersonResource.class);

    private final MemberPeople memberPeople;

    @Autowired
    public MemberPersonResource(MemberPeople memberPeople) {
        this.memberPeople = memberPeople;
    }

    @POST
    @Consumes()
    public void updateUser(MultivaluedMap<String, String> fields) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
        MemberPerson memberPerson = memberPeople.findByEMail(auth.getName());
        if (memberPerson != null) {
            memberPeople.save(
                    new MemberPerson(
                            memberPerson.getId(),
                            getOrElse(fields.get("firstName"), null),
                            getOrElse(fields.get("lastName"), null),
                            memberPerson.getPassword(),
                            getOrElse(fields.get("phoneNumber"), null),
                            getOrElse(fields.get("address"), null),
                            getOrElse(fields.get("email"), null),
                            memberPerson.getMemberCompany()));
        } else {
            WebApplicationException ex = new WebApplicationException(Response.Status.UNAUTHORIZED);
            logger.error("User " + auth.getName() + " does not exist");
            throw ex;
        }
    }

    private String getOrElse(List<String> values, String defaultValue) {
        if (values == null || values.size() <= 0)
            return defaultValue;
        if (values.size() > 1)
            throw new RuntimeException("More than one value received for field");
        return values.get(0);
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
