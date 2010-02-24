package no.java.portal.infrastructure.security;

import no.java.portal.domain.service.*;
import org.springframework.dao.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

public class CibusUserDetailsService /*implements UserDetailsService*/ {

	private final MemberDao memberDao;

    private static final GrantedAuthority roleUser = new GrantedAuthorityImpl("ROLE_USER");

    /*
    FilterChainProxy: FilterChainProxy[
        UrlMatcher = org.springframework.security.util.AntUrlPathMatcher[requiresLowerCase='true'];
        Filter Chains: {
            /**=[
                org.springframework.security.context.HttpSessionContextIntegrationFilter[ order=200; ],
                org.springframework.security.ui.logout.LogoutFilter[ order=300; ],
                org.springframework.security.ui.webapp.AuthenticationProcessingFilter[ order=700; ],
                org.springframework.security.ui.basicauth.BasicProcessingFilter[ order=1000; ],
                org.springframework.security.wrapper.SecurityContextHolderAwareRequestFilter[ order=1100; ],
                org.springframework.security.ui.rememberme.RememberMeProcessingFilter[ order=1200; ],
                org.springframework.security.providers.anonymous.AnonymousProcessingFilter[ order=1300; ],
                org.springframework.security.ui.ExceptionTranslationFilter[ order=1400; ],
                org.springframework.security.ui.SessionFixationProtectionFilter[ order=1600; ],
                org.springframework.security.intercept.web.FilterSecurityInterceptor@a92aaa
                ]
            }
        ]
     */

//	@Autowired
	public CibusUserDetailsService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        return null;
//		try {
//            MembershipNo membershipNo = memberDao.findMemberByEmail(emailAddress(userName));
//
//            GrantedAuthority[] auths = new GrantedAuthority[] {roleUser};
//            return new User(userName, null, true, true, true, true, auths);
//		} catch (UserNotFoundException e) {
//			throw new UsernameNotFoundException(e.getMessage(), e);
//		}
	}
}
