package com.ssachtleben.play.plugin.auth.providers;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import play.Application;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;

/**
 * Provides authentication with Facebook oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = FacebookAuthUser.class)
public class Facebook extends OAuth2Provider<FacebookAuthUser> {

	/**
	 * The unique provider name for {@link Facebook} provider.
	 */
	public static final String KEY = "facebook";

	public static abstract class FacebookSettingKeys {
		public static final String FIELDS = "fields";
	}

	/**
	 * Default constructor for {@link Facebook} provider and will be invoked during application startup if the provider is registered as
	 * plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public Facebook(Application app) throws MissingConfigurationException {
		super(app);
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
		return new FacebookAuthUser(data(token), token);
	}

	/**
	 * Fetch data from Facebook Graph with {@link Token} from current authentication process.
	 * 
	 * @param token
	 *          The {@link Token} to set.
	 * @return User data as {@link JsonNode} fetched from Facebook Graph.
	 */
	private JsonNode data(Token token) {
		String fields = config().getString(FacebookSettingKeys.FIELDS, "id,name");
		Response resp = request(token, Verb.GET, String.format("https://graph.facebook.com/me?fields=%s", fields));
		logger().info(
				String.format("Fetched data from Facebook Graph [success=%s, code=%d, content=%s]", resp.isSuccessful(), resp.getCode(),
						resp.getBody()));
		return toJson(resp.getBody());
	}

/**
	 * Converts Facebook Graph data response into {@link JsonNode].
	 * 
	 * @param data
	 *          The Facebook Graph data.
	 * @return Generated {@link JsonNode}.
	 */
	private JsonNode toJson(String data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readTree(data);
		} catch (IOException e) {
			logger().error("Failed to fetch data for facebook user", e);
		}
		return null;
	}
}
