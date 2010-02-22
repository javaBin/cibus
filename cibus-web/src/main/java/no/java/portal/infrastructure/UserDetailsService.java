package no.java.portal.infrastructure;

import no.java.portal.domain.User;
import no.java.portal.domain.UserNotFoundException;
import no.java.portal.domain.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 3, 2009
 */
@Component
public class UserDetailsService implements
		org.springframework.security.userdetails.UserDetailsService {

	private final Users users;
	
	@Autowired
	public UserDetailsService(Users users) {
		this.users = users;
	}

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		try {
			User user = users.getUser(userName);
			GrantedAuthority[] auths = new GrantedAuthority[] { new GrantedAuthorityImpl(
					"ROLE_USER") };
			org.springframework.security.userdetails.User userDetails = new org.springframework.security.userdetails.User(
					userName, user.getPassword(), true, true, true, true, auths);
			return userDetails;
		} catch (UserNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}

}
