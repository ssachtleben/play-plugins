package com.ssachtleben.play.plugin.auth.providers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi.Authenticate;
import org.scribe.model.Token;

import play.Application;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.TwitterAuthUser;

/**
 * Provides authentication with Twitter oauth1 interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = TwitterAuthUser.class)
public class Twitter extends OAuth1Provider<TwitterAuthUser> {

	/**
	 * The unique provider name for {@link Twitter} provider.
	 */
	public static final String KEY = "twitter";

	/**
	 * Default constructor for {@link Twitter} provider and will be invoked during application startup if the provider is registered as
	 * plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public Twitter(Application app) throws MissingConfigurationException {
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
		return Authenticate.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected TwitterAuthUser transform(Token token) {
		// TODO: Read data from twitter for creating new account or comparing current values.
		return new TwitterAuthUser(userId(token), token);
	}

	/**
	 * Extracts user id from {@link Token}.
	 * 
	 * @param token
	 *          The {@link Token} to set.
	 * @return The user id.
	 */
	private static String userId(Token token) {
		String user_id = null;
		String tmp = token.getRawResponse();
		String[] array = tmp.split("&");

		for (int i = 0; i < array.length; i++) {
			if (array[i].startsWith("user_id")) {
				user_id = (array[i].split("="))[1];
			}
		}
		return user_id;
	}
}
