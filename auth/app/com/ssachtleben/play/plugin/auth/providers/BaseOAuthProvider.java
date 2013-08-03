package com.ssachtleben.play.plugin.auth.providers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http.Request;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Provides access to authenticate with oauth interfaces via scribe.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseOAuthProvider<U extends OAuthAuthUser> extends BaseProvider<U> {

	/**
	 * New empty token used by some providers.
	 */
	protected static final Token EMPTY_TOKEN = null;

	/**
	 * Contains all oauth setting keys provided by application.conf. All keys must be configurated and will be checked during startup via
	 * {@link #validate()} and throws {@link MissingConfigurationException} if any setting key is missing.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class OAuthSettingKeys {

		/**
		 * The settings key for the public key.
		 */
		public static final String API_KEY = "key";

		/**
		 * The setting key for the secret key.
		 */
		public static final String API_SECRET = "secret";

		/**
		 * The setting key for the url back to our application called by the provider.
		 */
		public static final String CALLBACK = "callback";
	}

	/**
	 * Keeps {@link OAuthService} instance.
	 */
	protected OAuthService service;

	/**
	 * Returns the scribe provider class for {@link ServiceBuilder}.
	 * <p>
	 * It must implement the {@link Api} interface and is provided by scribe.
	 * </p>
	 * 
	 * @return The {@link Api} implementation class for this provider.
	 * @see https ://github.com/fernandezpablo85/scribe-java/tree/master/src/main/ java/org/scribe/builder/api
	 */
	public abstract Class<? extends Api> provider();

	/**
	 * Provides authentication url for this login try.
	 * 
	 * @return The url of the authentication provider.
	 */
	public abstract String authUrl();

	/**
	 * Creates {@link OAuthAuthUser} based on the given {@link OAuthAuthInfo}.
	 * 
	 * @param info
	 *          The {@link OAuthAuthInfo} to set.
	 * @return The transformed {@link OAuthAuthUser}.
	 */
	protected abstract U transform(final Token token);

	/**
	 * Retrieves {@link Token} from {@link Request}.
	 * 
	 * @param request
	 *          The {@link Request} to check.
	 * @return The {@link Token}.
	 */
	protected abstract Token tokenFromRequest(final Request request);

	/**
	 * Provides access {@link Token} for the current authentication process.
	 * 
	 * @param token
	 *          The token retrieved by provider.
	 * @param verifier
	 *          The verifier retrieved by provider.
	 * @return The {@link Token} instance.
	 */
	public Token accessToken(final String token, final String verifier) {
		return service().getAccessToken(token != null ? new Token(token, verifier) : null, new Verifier(verifier));
	}

	/**
	 * Send request via {@OAuthRequest}. TODO: Explain more...
	 * 
	 * @param accessToken
	 *          The {@link Token} to set.
	 * @param verb
	 *          The {@link Verb} to set.
	 * @param url
	 *          The url to set.
	 * @return The response with code and body.
	 */
	public Response request(final Token accessToken, final Verb verb, final String url) {
		OAuthRequest request = new OAuthRequest(verb, url);
		service().signRequest(accessToken, request);
		return request.send();
	}

	/**
	 * Provides {@link OAuthService} instance from scribe.
	 * 
	 * @return The {@link OAuthService} instance.
	 */
	public OAuthService service() {
		if (service == null) {
			final Configuration config = config();
			service = new ServiceBuilder().provider(provider()).apiKey(config.getString(OAuthSettingKeys.API_KEY))
					.apiSecret(config.getString(OAuthSettingKeys.API_SECRET)).callback(config.getString(OAuthSettingKeys.CALLBACK)).build();
		}
		return service;
	}

	/**
	 * Fetch data from OAuth interface with {@link Token} from current authentication process.
	 * 
	 * @param token
	 *          The {@link Token} to set.
	 * @return Fetched response data as {@link JsonNode}.
	 */
	protected JsonNode data(Token token, String url) {
		Response resp = request(token, Verb.GET, url);
		logger().info(
				String.format("Fetched response data [success=%s, code=%d, content=%s]", resp.isSuccessful(), resp.getCode(), resp.getBody()));
		return toJson(resp.getBody());
	}

/**
	 * Converts OAuth response data into {@link JsonNode].
	 * 
	 * @param data
	 *          The OAuth response data.
	 * @return Generated {@link JsonNode}.
	 */
	protected JsonNode toJson(String data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readTree(data);
		} catch (IOException e) {
			logger().error("Failed to fetch data", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#handle(play.mvc.Http.Context)
	 */
	@Override
	protected AuthUser handle(final Context context) {
		final Token token = tokenFromRequest(context.request());
		return token != null ? transform(token) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#settingKeys()
	 */
	@Override
	protected List<String> settingKeys() {
		return Arrays.asList(new String[] { OAuthSettingKeys.API_KEY, OAuthSettingKeys.API_SECRET, OAuthSettingKeys.CALLBACK });
	}
}
