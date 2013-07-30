package com.ssachtleben.play.plugin.auth.providers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.oauth.OAuthService;

import play.Application;
import play.Configuration;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;

/**
 * Wrap the scribe library to handle the oAuth process. The tokens are saved and retrieved in the user session. When an access token is
 * acquired the stored session values removed.
 * 
 * @author Alexander Kong
 * @author Sebastian Sachtleben
 */
public abstract class OAuthProvider extends BaseProvider {

	public static abstract class SettingKeys {
		public static final String API_KEY = "apiKey";
		public static final String API_SECRET = "apiSecret";
		public static final String API_CALLBACK = "apiCallback";
	}

	protected OAuthService service;

	public OAuthProvider(final Application app) throws Exception {
		super(app);
		validate();
	}

	public OAuthService service() throws MissingConfigurationException {
		if (service == null) {
			final Configuration config = config();
			service = new ServiceBuilder().provider(provider()).apiKey(config.getString(SettingKeys.API_KEY))
					.apiSecret(config.getString(SettingKeys.API_SECRET)).callback(config.getString(SettingKeys.API_CALLBACK)).build();
		}
		return service;
	}

	protected List<String> settingKeys() {
		return Arrays.asList(new String[] { SettingKeys.API_KEY, SettingKeys.API_SECRET, SettingKeys.API_CALLBACK });
	}

	private void validate() throws MissingConfigurationException {
		final Configuration config = config();
		Iterator<String> iter = settingKeys().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = config.getString(key);
			if (StringUtils.isEmpty(value)) {
				throw new MissingConfigurationException(String.format("Failed to initialize %s provider - Missing key: %s", key(), key));
			}
		}
	}

	public abstract Class<? extends Api> provider();
}
