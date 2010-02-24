package no.java.portal.page;

import no.java.portal.domain.member.*;
import no.java.portal.domain.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class UserAccountJspBean implements JspBean {

    private final MemberService memberService;

    @Autowired
    public UserAccountJspBean(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
