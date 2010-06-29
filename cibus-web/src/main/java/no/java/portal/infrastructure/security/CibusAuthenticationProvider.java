package no.java.portal.infrastructure.security;

import no.java.portal.domain.member.MemberPeople;
import no.java.portal.domain.member.MemberPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component("cibusAuthenticationProvider")
public class CibusAuthenticationProvider implements AuthenticationProvider {

    private static Collection<GrantedAuthority> authorities = Collections.<GrantedAuthority>singleton(new GrantedAuthorityImpl("ROLE_USER"));
    
    private final MemberPeople memberPeople;

    @Autowired
    public CibusAuthenticationProvider(MemberPeople memberPeople) {
        this.memberPeople = memberPeople;
    }

    // -----------------------------------------------------------------------
    // AuthenticationProvider Implementation
    // -----------------------------------------------------------------------

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String userName = (String) token.getPrincipal();
        String password = (String) authentication.getCredentials();

        MemberPerson person = memberPeople.findByEMailAndPassword(userName, password);
        
        if (person != null) {
            token = new UsernamePasswordAuthenticationToken(userName, password, authorities);
            token.setDetails(person);
            return token;
        } else {
            return authentication;
        }
    }

    public boolean supports(Class authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
