package no.java.portal.infrastructure.security;

import org.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class CibusAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(CibusAuthenticationProvider.class);

    // -----------------------------------------------------------------------
    // AuthenticationProvider Implementation
    // -----------------------------------------------------------------------

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("authentication = " + authentication);
        throw new RuntimeException("Not implemented");
    }

    public boolean supports(Class authentication) {
        return authentication.isInstance(UsernamePasswordAuthenticationToken.class);
    }
}
