package com.ssachtleben.play.plugin.auth.models;

import com.ssachtleben.play.plugin.auth.providers.EmailPassword;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link EmailPasswordAuthUser} contains {@code email} and {@code clearPassword} properties and is used by the {@link EmailPassword}
 * provider and will be passed to {@link AuthService}.
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see EmailPassword
 */
@SuppressWarnings("serial")
public class EmailPasswordAuthUser extends AuthUser {

	/**
	 * Keeps the email used during authentication process.
	 */
	private String email;

	/**
	 * Keeps the clear password used during authentication process.
	 */
	private String clearPassword;

	/**
	 * Default constructor for {@link EmailPasswordAuthUser}.
	 * 
	 * @param email
	 *          The email to set
	 * @param clearPassword
	 *          The clearPassword to set
	 */
	public EmailPasswordAuthUser(final String email, final String clearPassword) {
		this.email = email;
		this.clearPassword = clearPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#id()
	 */
	@Override
	public String id() {
		return email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return EmailPassword.KEY;
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
