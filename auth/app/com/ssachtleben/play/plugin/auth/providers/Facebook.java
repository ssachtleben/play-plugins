package com.ssachtleben.play.plugin.auth.providers;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;

/**
 * Provides authentication with Facebook oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = FacebookAuthUser.class)
public class Facebook extends BaseOAuth2Provider<FacebookAuthUser> {

	/**
	 * The unique provider name for {@link Facebook} provider.
	 */
	public static final String KEY = "facebook";

	public static abstract class FacebookSettingKeys {
		public static final String FIELDS = "fields";
	}

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
		return FacebookApi.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected FacebookAuthUser transform(Token token) {
		String fields = config().getString(FacebookSettingKeys.FIELDS, "id,name");
		return new FacebookAuthUser(me(token, fields), token);
	}

	/**
	 * Fetch data from Facebook Graph with {@link Token} and comma seperated list of properties in {@code fields}.
	 * 
	 * @param token
	 *          The {@link Token} to set.
	 * @param fields
	 *          The fields to set
	 * @return User data as {@link JsonNode} fetched from Facebook Graph.
	 */
	private JsonNode me(Token token, String fields) {
		return data(token, String.format("https://graph.facebook.com/me?fields=%s", fields));
	}
}
