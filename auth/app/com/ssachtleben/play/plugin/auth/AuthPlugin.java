package com.ssachtleben.play.plugin.auth;

import java.util.Iterator;
import java.util.Set;

import play.Application;
import play.Logger;
import play.Plugin;

import com.ssachtleben.play.plugin.auth.models.Identity;
import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

/**
 * Registers providers during application start.
 * 
 * @author Sebastian Sachtleben
 */
public class AuthPlugin extends Plugin {

	/**
	 * The logger for {@link AuthPlugin} class.
	 */
	private static final Logger.ALogger log = Logger.of(AuthPlugin.class);

	/**
	 * The current {@link Application} instance.
	 */
	private Application app;

	/**
	 * Default constructor for {@link AuthPlugin}.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 */
	public AuthPlugin(final Application app) {
		this.app = app;
	}

	/**
	 * @return Get current {@link Application} instance.
	 */
	public Application app() {
		return app;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStart()
	 */
	@Override
	public void onStart() {
		log.info(String.format("Starting %s for %s", this.getClass().getSimpleName(), app().toString()));
		final Set<BaseProvider<Identity>> providers = AuthUtils.findProviders();
		Iterator<BaseProvider<Identity>> iter = providers.iterator();
		while (iter.hasNext()) {
			BaseProvider<Identity> provider = iter.next();
			Providers.register(provider.key(), provider);
		}
	}
}
