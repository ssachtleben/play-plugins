package com.ssachtleben.play.plugin.auth.models;

import org.codehaus.jackson.JsonNode;
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
	 * @param token
	 *          The token to set
	 * @param data
	 *          The data to set
	 */
	public GoogleAuthUser(Token token, JsonNode data) {
		super(data.get("sub").asText(), token, data);
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
