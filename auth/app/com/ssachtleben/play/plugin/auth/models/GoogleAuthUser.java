package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Google;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link GoogleAuthUser} contains {@code id} and {@code token} properties.
 * <p>
 * The {@link Google} provider uses this model and pass it to {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see OAuthAuthUser
 * @see Google
 */
@SuppressWarnings("serial")
public class GoogleAuthUser extends OAuthAuthUser {

	/**
	 * Default constructor for {@link GoogleAuthUser}.
	 * 
	 * @param id
	 *          The id to set
	 * @param token
	 *          The token to set
	 */
	public GoogleAuthUser(String id, Token token) {
		super(id, token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return Google.KEY;
	}
}
