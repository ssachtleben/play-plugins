package com.ssachtleben.play.plugin.auth.providers;

import play.Application;
import play.Configuration;
import play.Plugin;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.Authenticator;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;

/**
 * Register all authentication provider as Play {@link Plugin} and provides the {{@link #authenticate(Context, Object)} method.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseProvider extends Plugin {

	private Application application;

	public BaseProvider(final Application app) {
		application = app;
	}

	public abstract String key();

	protected Configuration config() throws MissingConfigurationException {
		final Configuration authConfig = Authenticator.config(app());
		final Configuration providerConfig = authConfig.getConfig(key());
		if (providerConfig == null) {
			throw new MissingConfigurationException(String.format("Missing setting key '%s.%s' in application.conf",
					Authenticator.SETTING_KEY_AUTH, key()));
		}
		return providerConfig;
	}

	public Application app() {
		return application;
	}
}