package com.ssachtleben.play.plugin.auth.builders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * Provides basic oauth2 implementation.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseOAuth2 extends DefaultApi20 {

	public abstract String authorizeEndpoint();

	public abstract String accessTokenEndpoint();

	public abstract String tokenPattern();

	public String getAccessTokenQueryName() {
		return OAuthConstants.ACCESS_TOKEN;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return accessTokenEndpoint();
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new AccessTokenExtractor() {

			public Token extract(String response) {
				Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

				Matcher matcher = Pattern.compile(tokenPattern()).matcher(response);
				if (matcher.find()) {
					String token = OAuthEncoder.decode(matcher.group(1));
					return new Token(token, "", response);
				} else {
					throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
				}
			}
		};
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		if (config.hasScope()) {
			return String.format(authorizeUrl() + "&scope=%s", config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
					OAuthEncoder.encode(config.getScope()));
		} else {
			return String.format(authorizeUrl(), config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
		}
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		return new OAuth2Service(this, config);
	}

	private String authorizeUrl() {
		return authorizeEndpoint() + "&client_id=%s&redirect_uri=%s";
	}

	private class OAuth2Service extends OAuth20ServiceImpl {

		private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
		private static final String GRANT_TYPE = "grant_type";
		private DefaultApi20 api;
		private OAuthConfig config;

		public OAuth2Service(DefaultApi20 api, OAuthConfig config) {
			super(api, config);
			this.api = api;
			this.config = config;
		}

		@Override
		public Token getAccessToken(Token requestToken, Verifier verifier) {
			OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
			switch (api.getAccessTokenVerb()) {
			case POST:
				request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
				// TODO HA: API Secret is optional
				if (config.getApiSecret() != null && config.getApiSecret().length() > 0)
					request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
				request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
				request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
				request.addBodyParameter(GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
				break;
			case GET:
			default:
				request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
				// TODO HA: API Secret is optional
				if (config.getApiSecret() != null && config.getApiSecret().length() > 0)
					request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
				request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
				request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
				if (config.hasScope())
					request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
			}
			Response response = request.send();
			return api.getAccessTokenExtractor().extract(response.getBody());
		}

		@Override
		public void signRequest(Token accessToken, OAuthRequest request) {
			request.addQuerystringParameter(getAccessTokenQueryName(), accessToken.getToken());
		}

	}

}