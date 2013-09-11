package com.ssachtleben.play.plugin.auth.providers;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.builders.Google2Api;
import com.ssachtleben.play.plugin.auth.exceptions.AuthenticationException;
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

	/**
	 * The default scope for {@link Google} oauth provider. TODO: this should be used if no scope is set in application.conf...
	 */
	public static final String DEFAULT_SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

	/**
	 * The userinfo url to fetch user data during authentication process.
	 */
	private static final String RESOURCE_URL = "https://www.googleapis.com/oauth2/v3/userinfo?alt=json";

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
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#defaultScope()
	 */
	@Override
	protected String defaultScope() {
		return DEFAULT_SCOPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.scribe.model.Token)
	 */
	@Override
	protected GoogleAuthUser transform(Token token) throws AuthenticationException {
		JsonNode data = data(token, RESOURCE_URL);
		logger().info("Retrieved: " + data.toString());
		return new GoogleAuthUser(token, data);
	}
}
