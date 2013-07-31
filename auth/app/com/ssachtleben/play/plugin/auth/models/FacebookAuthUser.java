package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link FacebookAuthUser} contains {@code id} and {@code token} properties.
 * <p>
 * The {@link Facebook} provider uses this model and pass it to {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see OAuthAuthUser
 * @see Facebook
 */
@SuppressWarnings("serial")
public class FacebookAuthUser extends OAuthAuthUser {

	/**
	 * Default constructor for {@link FacebookAuthUser}.
	 * 
	 * @param id
	 *          The id to set
	 * @param token
	 *          The token to set
	 */
	public FacebookAuthUser(String id, Token token) {
		super(id, token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return Facebook.KEY;
	}
}
