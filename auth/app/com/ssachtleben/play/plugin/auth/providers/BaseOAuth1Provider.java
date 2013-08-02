package com.ssachtleben.play.plugin.auth.providers;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;

import play.mvc.Http.Request;

import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Handle authentication process with oauth1.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseOAuth1Provider<U extends OAuthAuthUser> extends BaseOAuthProvider<U> {

	/**
	 * Contains all request parameter names.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class RequestParameter {
		public static final String OAUTH_TOKEN = "oauth_token";
		public static final String OAUTH_VERIFIER = "oauth_verifier";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return service().getAuthorizationUrl(service().getRequestToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#tokenFromRequest(play.mvc.Http.Request)
	 */
	@Override
	protected Token tokenFromRequest(final Request request) {
		final String oAuthToken = request.getQueryString(RequestParameter.OAUTH_TOKEN);
		final String oAuthVerifier = request.getQueryString(RequestParameter.OAUTH_VERIFIER);
		if (!StringUtils.isEmpty(oAuthToken) && !StringUtils.isEmpty(oAuthVerifier)) {
			return accessToken(oAuthToken, oAuthVerifier);
		}
		return null;
	}
}
