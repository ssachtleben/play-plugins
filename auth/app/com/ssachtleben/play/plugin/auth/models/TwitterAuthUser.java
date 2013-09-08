package com.ssachtleben.play.plugin.auth.models;

import org.codehaus.jackson.JsonNode;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.Twitter;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link TwitterAuthUser} contains {@code id} and {@code token} properties.
 * <p>
 * The {@link Twitter} provider uses this model and pass it to {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see OAuthAuthUser
 * @see Facebook
 */
@SuppressWarnings("serial")
public class TwitterAuthUser extends OAuthAuthUser {

	/**
	 * Default constructor for {@link TwitterAuthUser}.
	 * 
	 * @param id
	 *          The id to set
	 * @param token
	 *          The token to set
	 */
	public TwitterAuthUser(final String id, final Token token, final JsonNode data) {
		super(id, token, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return Twitter.KEY;
	}
}
