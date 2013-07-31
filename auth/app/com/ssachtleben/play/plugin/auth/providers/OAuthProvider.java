package com.ssachtleben.play.plugin.auth.providers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Wrap the scribe library to handle the oAuth process. The tokens are saved and retrieved in the user session. When an access token is
 * acquired the stored session values removed.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class OAuthProvider<U extends OAuthAuthUser, I extends OAuthAuthInfo> extends BaseProvider<U, I> {

	/**
	 * Contains all setting keys provided by application.conf.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class SettingKeys {
		public static final String API_KEY = "key";
		public static final String API_SECRET = "secret";
		public static final String API_CALLBACK = "callback";
	}

	/**
	 * Keeps {@link OAuthService} instance.
	 */
	protected OAuthService service;

	/**
	 * Creates new {@link OAuthProvider} instance and validates setting keys in application.conf.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws Exception
	 */
	public OAuthProvider(final Application app) throws Exception {
		super(app);
		validate();
		logger().info("Provider initialized");
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
	 * <p>
	 * This is step 1 of 4.
	 * </p>
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
	 * Provides access {@link Token} for the current authentication process.
	 * <p>
	 * This is step 2 of 4.
	 * </p>
	 * 
	 * @param token
	 *          The token retrieved by provider.
	 * @param verifier
	 *          The verifier retrieved by provider.
	 * @return The {@link Token} instance.
	 */
	public Token accessToken(String token, String verifier) {
		try {
			return service().getAccessToken(token != null ? new Token(token, verifier) : null, new Verifier(verifier));
		} catch (Exception e) {
			logger().warn("Error retrieving access token", e);
		}
		return null;
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
	public Response request(Token accessToken, Verb verb, String url) {
		OAuthRequest request = new OAuthRequest(verb, url);
		service.signRequest(accessToken, request);
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
			service = new ServiceBuilder().provider(provider()).apiKey(config.getString(SettingKeys.API_KEY))
					.apiSecret(config.getString(SettingKeys.API_SECRET)).callback(config.getString(SettingKeys.API_CALLBACK)).build();
		}
		return service;
	}

	/**
	 * List of setting keys which must be provided from the application.conf. These keys will be validated during application start.
	 * 
	 * @return List of setting keys.
	 */
	protected List<String> settingKeys() {
		return Arrays.asList(new String[] { SettingKeys.API_KEY, SettingKeys.API_SECRET, SettingKeys.API_CALLBACK });
	}

	/**
	 * Validates {@link OAuthProvider} during application start and checks if all setting keys properly set in application.conf.
	 * 
	 * @throws MissingConfigurationException
	 *           The exception throws for missing setting keys in application.conf.
	 */
	private void validate() throws MissingConfigurationException {
		final List<String> settingKeys = settingKeys();
		if (settingKeys.size() == 0) {
			return;
		}
		final Configuration config = config();
		Iterator<String> iter = settingKeys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = config.getString(key);
			if (StringUtils.isEmpty(value)) {
				throw new MissingConfigurationException(String.format(
						"Failed to initialize %s provider due missing settings key '%s.%s.%s' in application.conf", key(), Auth.SETTING_KEY_AUTH,
						key(), key));
			}
		}
	}
}
