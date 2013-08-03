package com.ssachtleben.play.plugin.auth.providers;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import play.Configuration;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.builders.Google2Api;
import com.ssachtleben.play.plugin.auth.models.GoogleAuthUser;

/**
 * Provides authentication with Google oauth2 interface.
 * <p>
 * <b>Important:</b> This provider is untested and may not work properly...
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = GoogleAuthUser.class)
public class Google extends BaseOAuth2Provider<GoogleAuthUser> {

	/**
	 * The unique provider name for {@link Google} provider.
	 */
	public static final String KEY = "google";

	private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

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
		return Google2Api.class;
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
					.apiSecret(config.getString(OAuthSettingKeys.API_SECRET)).callback(config.getString(OAuthSettingKeys.CALLBACK)).scope(SCOPE)
					.build();
		}
		return service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected GoogleAuthUser transform(Token token) {
		JsonNode data = data(token, "https://www.googleapis.com/oauth2/v3/userinfo?alt=json");
		logger().info("Retrieved: " + data.toString());
		return new GoogleAuthUser(token, data);
	}
}
