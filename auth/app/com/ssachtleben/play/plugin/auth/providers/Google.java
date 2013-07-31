package com.ssachtleben.play.plugin.auth.providers;

import java.util.Arrays;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import play.Application;
import play.Configuration;

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
	private static final String SCOPE = "https://docs.google.com/feeds/";

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
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#service()
	 */
	public OAuthService service() {
		if (service == null) {
			final Configuration config = config();
			service = new ServiceBuilder().provider(provider()).apiKey(config.getString(OAuthSettingKeys.API_KEY))
					.apiSecret(config.getString(OAuthSettingKeys.API_SECRET)).scope(SCOPE).build();
		}
		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return AUTHORIZE_URL + service().getRequestToken();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#info(org.scribe .model.Token)
	 */
	@Override
	protected OAuthAuthInfo info(Token token) {
		// TODO: Read data from facebook for creating new account or comparing
		// current values.
		return new OAuthAuthInfo(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(com .ssachtleben.play.plugin.auth.models.OAuthAuthInfo)
	 */
	@Override
	protected GoogleAuthUser transform(OAuthAuthInfo info) {
		// TODO: Create proper google auth user...
		return new GoogleAuthUser("123", info.token());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#settingKeys()
	 */
	@Override
	protected List<String> settingKeys() {
		return Arrays.asList(new String[] { OAuthSettingKeys.API_KEY, OAuthSettingKeys.API_SECRET });
	}
}
