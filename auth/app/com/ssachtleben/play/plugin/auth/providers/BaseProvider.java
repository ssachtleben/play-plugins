package com.ssachtleben.play.plugin.auth.providers;

import play.Application;
import play.Configuration;
import play.Plugin;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.Authenticator;

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

	protected Configuration config() {
		return Authenticator.config().getConfig(key());
	}

	public Application app() {
		return application;
	}
}