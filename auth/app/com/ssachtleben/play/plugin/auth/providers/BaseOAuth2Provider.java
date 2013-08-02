package com.ssachtleben.play.plugin.auth.providers;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

import play.Application;
import play.mvc.Http.Request;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Handle authentication process with oauth2.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseOAuth2Provider<U extends OAuthAuthUser> extends BaseOAuthProvider<U> {

	/**
	 * Contains all request parameter names.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class RequestParameter {
		public static final String CODE = "code";
	}

	/**
	 * Default constructor for {@link BaseOAuth2Provider} provider and will be invoked during application startup if the provider is registered as
	 * plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public BaseOAuth2Provider(Application app) throws MissingConfigurationException {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return service().getAuthorizationUrl(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#tokenFromRequest(play.mvc.Http.Request)
	 */
	@Override
	protected Token tokenFromRequest(final Request request) {
		final String code = request.getQueryString(RequestParameter.CODE);
		if (!StringUtils.isEmpty(code)) {
			return service().getAccessToken(null, new Verifier(code));
		}
		return null;
	}
}