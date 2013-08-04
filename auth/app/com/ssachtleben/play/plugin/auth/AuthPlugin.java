package com.ssachtleben.play.plugin.auth;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

import play.Application;
import play.Logger;
import play.Plugin;
import play.libs.Akka;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
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
		log.debug("Plugin created");
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
		log.debug("Start Plugin");
		if (!app.configuration().getBoolean("auth.scanner.active", Boolean.TRUE)) {
			return;
		}
		if (app.configuration().getBoolean("auth.scanner.async", Boolean.TRUE)) {
			Akka.future(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					findProviders();
					return null;
				}
			});
		} else {
			findProviders();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStop()
	 */
	@Override
	public void onStop() {
		log.debug("Stop Plugin");
		Providers.clear();
	}

	/**
	 * Find Providers and auth methods.
	 */
	private void findProviders() {
		final Set<BaseProvider<Identity>> providers = AuthUtils.findProviders();
		final Set<Method> authMethods = AuthUtils.findAuthMethods();
		Iterator<BaseProvider<Identity>> iter = providers.iterator();
		while (iter.hasNext()) {
			BaseProvider<Identity> provider = iter.next();
			Method authMethod = null;
			Iterator<Method> iter2 = authMethods.iterator();
			while (iter2.hasNext()) {
				Method currMethod = iter2.next();
				Authenticates authAnnotation = currMethod.getAnnotation(Authenticates.class);
				if (provider.key().equals(authAnnotation.provider())) {
					// TODO: validate if method got matching parameters...
					authMethod = currMethod;
					log.info(String.format("Found auth method for %s provider: %s", provider.key(), authMethod));
					break;
				}
			}
			Providers.register(provider.key(), provider, authMethod);
		}
	}
}
