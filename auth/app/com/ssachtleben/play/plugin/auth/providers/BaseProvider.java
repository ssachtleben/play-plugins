package com.ssachtleben.play.plugin.auth.providers;

import play.Application;
import play.Plugin;
import play.mvc.Http.Context;

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

	public abstract String getKey();

	public abstract Object authenticate(final Context context, final Object payload);

	public Application getApplication() {
		return application;
	}
}