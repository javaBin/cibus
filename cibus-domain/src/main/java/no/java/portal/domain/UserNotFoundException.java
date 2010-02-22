package no.java.portal.domain;

/**
 * @author <a href="mailto:thor.aage.eldby@arktekk.no">Thor &Aring;ge Eldby</a>
 * @since Mar 3, 2009
 */
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 9013449595515152441L;

	public UserNotFoundException(String msg) {
		super(msg);
	}

}
