package com.ssachtleben.play.plugin.auth.providers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;
import play.mvc.Http.Context;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.models.AuthInfo;
import com.ssachtleben.play.plugin.auth.models.AuthUser;

/**
 * Registers provider as Play {@link Plugin}.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseProvider<U extends AuthUser, I extends AuthInfo> extends Plugin {

	@SuppressWarnings("rawtypes")
	public abstract static class Providers {
		private static final Logger.ALogger log = Logger.of(Providers.class);
		private static Map<String, BaseProvider> cache = new HashMap<String, BaseProvider>();

		public static void register(final String name, final BaseProvider provider) {
			final Object previous = cache.put(name, provider);
			if (previous != null) {
				log.warn(String.format("There are multiple auth providers registered for key '%s'", name));
			}
		}

		public static void unregister(final String name) {
			cache.remove(name);
		}

		public static BaseProvider get(final String name) {
			return cache.get(name);
		}

		public static Collection<BaseProvider> list() {
			return cache.values();
		}

		public static boolean has(final String name) {
			return cache.containsKey(name);
		}
	}

	/**
	 * Keeps {@link Application} instance.
	 */
	private Application app;

	/**
	 * Default contructor for {@link BaseProvider} and will be invoked during application startup.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 */
	public BaseProvider(final Application app) {
		this.app = app;
	}

	/**
	 * Provides the current {@link Application} instance.
	 * 
	 * @return The {@link Application} instance.
	 */
	protected Application app() {
		return app;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStart()
	 */
	@Override
	public void onStart() {
		logger().info("Register auth plugin");
		Providers.register(key(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStop()
	 */
	@Override
	public void onStop() {
		logger().info("Unregister auth plugin");
		Providers.unregister(key());
	}

	/**
	 * Provides access to settings from application.conf.
	 * 
	 * @return The provider {@link Configuration}.
	 */
	protected Configuration config() {
		final Configuration config = Auth.config(app());
		if (config != null) {
			return config.getConfig(key());
		}
		return null;
	}

	/**
	 * Returns logger from provider instance.
	 * 
	 * @return The {@link Logger.ALogger} instance.
	 */
	protected Logger.ALogger logger() {
		return Logger.of(WordUtils.capitalize(String.format("%s.class", key())));
	}

	/**
	 * Provides the unqiue key for this {@link BaseProvider}.
	 * 
	 * @return The unqiue key.
	 */
	public abstract String key();

	/**
	 * Handles login with this provider.
	 * 
	 * @param context
	 *          The current {@link Context}.
	 * @return Play {@link Result} object.
	 */
	public abstract Result login(final Context context);
}