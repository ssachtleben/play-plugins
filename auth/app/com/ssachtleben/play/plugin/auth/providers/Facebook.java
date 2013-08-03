package com.ssachtleben.play.plugin.auth.providers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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

	/**
	 * The Facebook Graph url.
	 */
	public static final String GRAPH_URL = "https://graph.facebook.com";

	/**
	 * Provides setting keys for {@link Facebook} provider.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class FacebookSettingKeys {

		/**
		 * The fields fetched from graph during authentication.
		 */
		public static final String FIELDS = "fields";

	}

	/**
	 * Provides default values for settings keys of {@link Facebook} provider.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class FacebookSettingDefault {

		/**
		 * The default scope for {@link Facebook} oauth provider.
		 */
		public static final String SCOPE = "email";

		/**
		 * The default fields for {@link Facebook} oauth provider.
		 */
		public static final String FIELDS = "id,name,email";

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
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#defaultScope()
	 */
	@Override
	protected String defaultScope() {
		return FacebookSettingDefault.SCOPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected FacebookAuthUser transform(Token token) {
		String fields = config().getString(FacebookSettingKeys.FIELDS, FacebookSettingDefault.FIELDS);
		JsonNode data = null;
		if (fields == null || !"".equals(fields)) {
			data = me(token, fields);
		} else {
			data = new ObjectMapper().createObjectNode();
		}
		logger().info("Retrieved: " + data.toString());
		return new FacebookAuthUser(token, data);
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
		return data(token, String.format("%s/me?fields=%s", GRAPH_URL, fields));
	}
}
