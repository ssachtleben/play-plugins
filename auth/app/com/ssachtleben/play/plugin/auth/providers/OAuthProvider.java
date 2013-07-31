package com.ssachtleben.play.plugin.auth.providers;

import java.util.Arrays;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import play.Application;
import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http.Request;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Provides access to authenticate with oauth interfaces via scribe.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class OAuthProvider<U extends OAuthAuthUser, I extends OAuthAuthInfo> extends BaseProvider<U, I> {

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
	 * Default constructor for {@link OAuthProvider} provider and will be invoked during application startup if the provider is registered as
	 * plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 * @see OAuth1Provider
	 * @see OAuth2Provider
	 */
	public OAuthProvider(final Application app) throws MissingConfigurationException {
		super(app);
	}

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
	 * Creates {@link OAuthAuthInfo} based on the {@link Token}.
	 * 
	 * @param token
	 *          The {@link Token} to set.
	 * @return The {@link OAuthAuthInfo}.
	 */
	protected abstract I info(final Token token);

	/**
	 * Creates {@link OAuthAuthUser} based on the given {@link OAuthAuthInfo}.
	 * 
	 * @param info
	 *          The {@link OAuthAuthInfo} to set.
	 * @return The transformed {@link OAuthAuthUser}.
	 */
	protected abstract U transform(final I info);

	/**
	 * Retrieves {@link Token} from {@link Request}.
	 * 
	 * @param request
	 *          The {@link Request} to check.
	 * @return The {@link Token}.
	 */
	protected abstract Token retrieveTokenFromRequest(final Request request);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#handle(play.mvc.Http.Context)
	 */
	@Override
	protected AuthUser handle(final Context context) {
		final Token token = retrieveTokenFromRequest(context.request());
		logger().debug("Found token: " + token.toString());
		final I info = info(token);
		if (info != null) {
			logger().debug("Found info: " + info.toString());
			return transform(info);
		}
		return null;
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
