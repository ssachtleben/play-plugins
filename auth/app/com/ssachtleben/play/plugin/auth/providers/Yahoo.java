package com.ssachtleben.play.plugin.auth.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.exceptions.AuthenticationException;
import com.ssachtleben.play.plugin.auth.models.YahooAuthUser;

/**
 * Provides authentication with Yahoo oauth interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = YahooAuthUser.class)
public class Yahoo extends BaseOAuth1Provider<YahooAuthUser> {

	/**
	 * The unique provider name for {@link Yahoo} provider.
	 */
	public static final String KEY = "yahoo";

	/**
	 * The guid url.
	 */
	public static final String GUID_URL = "http://social.yahooapis.com/v1/me/guid?format=json";

	/**
	 * The resource url.
	 */
	public static final String RESOURCE_URL = "http://social.yahooapis.com/v1/user/%s/profile?format=json";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#key()
	 */
	@Override
	public String key() {
		return KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#provider()
	 */
	@Override
	public Class<? extends Api> provider() {
		return YahooApi.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected YahooAuthUser transform(Token token) throws AuthenticationException {
		JsonNode guidNode = data(token, GUID_URL);
		if (guidNode == null || guidNode.get("guid") == null || guidNode.get("guid").get("value") == null) {
			throw new AuthenticationException(String.format("Failed to retrieve guid from this response: %s", guidNode));
		}
		String guid = guidNode.get("guid").get("value").asText();
		JsonNode data = data(token, String.format(RESOURCE_URL, guidNode.get("guid").get("value").asText()));
		logger().info("Retrieved: " + data.toString());
		return new YahooAuthUser(guid, token, data);
	}
}
