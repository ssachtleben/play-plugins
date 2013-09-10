package com.ssachtleben.play.plugin.auth.models;

import org.codehaus.jackson.JsonNode;

import com.ssachtleben.play.plugin.auth.providers.PasswordEmail;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link PasswordEmailAuthUser} contains {@code email} and {@code clearPassword} properties and is used by the {@link PasswordEmail}
 * provider and will be passed to {@link AuthService}.
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see PasswordEmail
 */
@SuppressWarnings("serial")
public class PasswordEmailAuthUser extends PasswordAuthUser {

	/**
	 * Keeps the email used during authentication process.
	 */
	private String email;

	/**
	 * Default constructor for {@link PasswordEmailAuthUser}.
	 * 
	 * @param email
	 *          The email to set
	 * @param clearPassword
	 *          The clearPassword to set
	 */
	public PasswordEmailAuthUser(final String email, final String clearPassword, final JsonNode data) {
		super(clearPassword);
		this.email = email;
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return PasswordEmail.KEY;
	}

	/**
	 * @return the email
	 */
	public String email() {
		return email;
	}
}
