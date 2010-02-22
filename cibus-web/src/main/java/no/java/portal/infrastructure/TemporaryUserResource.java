package no.java.portal.infrastructure;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import no.java.portal.domain.Categories;
import no.java.portal.domain.SubscribedCategory;
import no.java.portal.domain.User;
import no.java.portal.domain.UserNotFoundException;
import no.java.portal.domain.Users;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 31, 2009
 */
@Component
@Path("/temporary/user")
public class TemporaryUserResource {

    private static final Log logger = LogFactory.getLog(TemporaryUserResource.class);

    private Categories categories;

    private Users users;

    private Ehcache temporaryUserCache;

    private MimeMessageHelper templateEMailVerificationMessage;

    private JavaMailSender mailSender;

    private String contextPath;

    @Autowired
    public TemporaryUserResource(Categories categories, Users users, @Qualifier("temporaryUserCache") Ehcache temporaryUserCache,
            MimeMessageHelper templateEMailVerificationMessage, JavaMailSender mailSender) {
        super();
        this.categories = categories;
        this.users = users;
        this.temporaryUserCache = temporaryUserCache;
        this.templateEMailVerificationMessage = templateEMailVerificationMessage;
        this.mailSender = mailSender;
    }

    @Configure
    public void configure(@Configuration(expression = "mail.contextPath") String contextPath) {
        this.contextPath = contextPath;
    }
    
    @POST
    public void registerUser(MultivaluedMap<String, String> selectedCategories) {
        User user = new User(null, null, Collections.<SubscribedCategory> emptyList(), null, UUID.randomUUID().toString());
        User newUser = UserResource.createUserObject(user, categories, selectedCategories);
        try {
            users.getUser(newUser.getUserName());
            WebApplicationException ex = new WebApplicationException(Response.Status.BAD_REQUEST);
            logger.error("Permanent user " + newUser.getUserName() + " already exists", ex);
            throw ex;
        } catch (UserNotFoundException e) {
            // Expected
        }
        if (temporaryUserCache.get(newUser.getUserName()) != null) {
            WebApplicationException ex = new WebApplicationException(Response.Status.BAD_REQUEST);
            logger.error("Temporary user " + newUser.getUserName() + " already exists", ex);
            throw ex;
        }
        temporaryUserCache.put(new Element(newUser.getUserName(), newUser));
        temporaryUserCache.put(new Element(newUser.getUserkey(), newUser));
        sendMail(newUser);
    }

    private void sendMail(User newUser) {
        try {
            MimeMailMessage mailMsg = new MimeMailMessage(templateEMailVerificationMessage);
            String messageTemplate = IOUtils.toString(getClass().getResourceAsStream("/user-registration-verfication-email.txt"));
            String message = MessageFormat.format(messageTemplate, newUser.getUserName(), newUser.getEmail(), newUser.getUserkey(),
                    contextPath);
            logger.debug("Message to " + newUser.getEmail() + ": " + message);
            mailMsg.setText(message);
            mailMsg.setFrom("portal@java.no"); // TODO: Externalize
            mailMsg.setTo(newUser.getEmail());
            mailSender.send(mailMsg.getMimeMessage());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read user registration email template");
        }
    }

}
