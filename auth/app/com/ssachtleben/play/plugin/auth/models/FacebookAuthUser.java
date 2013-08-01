package com.ssachtleben.play.plugin.auth.models;

import org.codehaus.jackson.JsonNode;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link FacebookAuthUser} contains {@code id}, {@code token} and {@code data} properties. The data {@link JsonNode} contains all
 * fetched information based on 'auth.facebook.fields' property in application.conf
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
	 * Keeps {@link JsonNode} with user data fetched during authentication from facebook graph.
	 */
	private JsonNode data;

	/**
	 * Default constructor for {@link FacebookAuthUser}.
	 * 
	 * @param data
	 *          The data to set
	 * @param token
	 *          The token to set
	 */
	public FacebookAuthUser(JsonNode data, Token token) {
		super(data.get("id").asText(), token);
		this.data = data;
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

	/**
	 * @return {@link JsonNode} with user data fetched during authentication from facebook graph.
	 */
	public JsonNode data() {
		return data;
	}

}
