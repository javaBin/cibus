package no.java.portal.infrastructure.security;

import no.java.portal.domain.member.Member.*;
import static no.java.portal.domain.member.Member.EmailAddress.*;
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
        EmailAddress emailAddress = emailAddress((String) token.getPrincipal());

        CibusAuthentication cibusAuthentication = authenticationService.isAuthenticated(emailAddress, password);

        if (cibusAuthentication.member.isSome()) {
            return new UsernamePasswordAuthenticationToken(emailAddress.toString(), password, authorities);
        } else {
            return authentication;
        }
    }

    public boolean supports(Class authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
