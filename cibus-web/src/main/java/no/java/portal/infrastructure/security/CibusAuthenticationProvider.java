package no.java.portal.infrastructure.security;

import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.MailAddress.*;
import no.java.portal.domain.service.*;
import no.java.portal.domain.service.AuthenticationService.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component("cibusAuthenticationProvider")
public class CibusAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(CibusAuthenticationProvider.class);

    private final AuthenticationService authenticationService;

    private static Collection<GrantedAuthority> authorities = Collections.<GrantedAuthority>singleton(new GrantedAuthorityImpl("ROLE_USER"));

    @Autowired
    public CibusAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // -----------------------------------------------------------------------
    // AuthenticationProvider Implementation
    // -----------------------------------------------------------------------

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String password = (String) authentication.getCredentials();
        MailAddress mailAddress = mailAddress((String) token.getPrincipal());

        CibusAuthentication cibusAuthentication = authenticationService.isAuthenticated(mailAddress, password);

        if (cibusAuthentication.member.isSome()) {
            token = new UsernamePasswordAuthenticationToken(mailAddress.toString(), password, authorities);
            token.setDetails(cibusAuthentication.member.some());
            return token;
        } else {
            return authentication;
        }
    }

    public boolean supports(Class authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
