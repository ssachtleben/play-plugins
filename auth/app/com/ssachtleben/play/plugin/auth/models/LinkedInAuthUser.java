package com.ssachtleben.play.plugin.auth.models;

import org.codehaus.jackson.JsonNode;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.LinkedIn;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link LinkedInAuthUser} contains {@code id}, {@code token} and {@code data} properties. The data {@link JsonNode} contains all
 * fetched information based on choosen scope.
 * <p>
 * The {@link LinkedIn} provider uses this model and pass it to {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see OAuthAuthUser
 * @see Facebook
 */
@SuppressWarnings("serial")
public class LinkedInAuthUser extends OAuthAuthUser {

	/**
	 * Default constructor for {@link LinkedInAuthUser}.
	 * 
	 * @param token
	 *          The token to set
	 * @param data
	 *          The data to set
	 */
	public LinkedInAuthUser(final Token token, final JsonNode data) {
		super(data.get("id").asText(), token, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
	 */
	@Override
	public String provider() {
		return LinkedIn.KEY;
	}
}
