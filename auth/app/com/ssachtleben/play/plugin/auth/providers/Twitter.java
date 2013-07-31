package com.ssachtleben.play.plugin.auth.providers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi.Authenticate;
import org.scribe.model.Token;

import play.Application;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.TwitterAuthUser;

/**
 * Provides authentication with Twitter oauth1 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Twitter extends OAuth1Provider<TwitterAuthUser, OAuthAuthInfo> {
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
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuth1Provider#info(org.scribe.model.Token)
	 */
	@Override
	protected OAuthAuthInfo info(Token token) {
		// TODO: Read data from twitter for creating new account or comparing current values.
		return new OAuthAuthInfo(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuth1Provider#transform(com.ssachtleben.play.plugin.auth.models.OAuth1AuthInfo)
	 */
	@Override
	protected TwitterAuthUser transform(OAuthAuthInfo info) {
		return new TwitterAuthUser(info.token());
	}
}
