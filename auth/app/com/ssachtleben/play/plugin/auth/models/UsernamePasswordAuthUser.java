package com.ssachtleben.play.plugin.auth.models;

import com.ssachtleben.play.plugin.auth.providers.EmailPassword;
import com.ssachtleben.play.plugin.auth.providers.UsernamePassword;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link UsernamePasswordAuthUser} contains {@code username} and {@code clearPassword} properties and is used by the
 * {@link EmailPassword} provider and will be passed to {@link AuthService}.
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see EmailPassword
 */
@SuppressWarnings("serial")
public class UsernamePasswordAuthUser extends AuthUser {

	/**
	 * Keeps the username used during authentication process.
	 */
	private String username;

	/**
	 * Keeps the clear password used during authentication process.
	 */
	private String clearPassword;

	/**
	 * Default constructor for {@link UsernamePasswordAuthUser}.
	 * 
	 * @param username
	 *          The username to set
	 * @param clearPassword
	 *          The clearPassword to set
	 */
	public UsernamePasswordAuthUser(final String username, final String clearPassword) {
		this.username = username;
		this.clearPassword = clearPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#id()
	 */
	@Override
	public String id() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return UsernamePassword.KEY;
	}

	/**
	 * The clear password for authentication purpose.
	 * 
	 * @return The clear password.
	 */
	public String clearPassword() {
		return clearPassword;
	}
}
