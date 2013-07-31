package com.ssachtleben.play.plugin.auth.providers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;

import play.Application;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.GoogleAuthUser;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;

/**
 * Provides authentication with Google oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Google extends OAuth2Provider<GoogleAuthUser, OAuthAuthInfo> {
	public static final String KEY = "Google";

	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";

	/**
	 * Default constructor for {@link Google} provider and will be invoked during application startup if the provider is registered as plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public Google(Application app) throws MissingConfigurationException {
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
		return GoogleApi.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return AUTHORIZE_URL + service.getRequestToken();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#info(org.scribe .model.Token)
	 */
	@Override
	protected OAuthAuthInfo info(Token token) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(com .ssachtleben.play.plugin.auth.models.OAuthAuthInfo)
	 */
	@Override
	protected GoogleAuthUser transform(OAuthAuthInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
}
