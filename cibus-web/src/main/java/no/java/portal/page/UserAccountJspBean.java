package no.java.portal.page;

import no.java.portal.domain.member.MemberPerson;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class UserAccountJspBean implements JspBean {

    public MemberPerson getMember() {
        return (MemberPerson) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
    
}
