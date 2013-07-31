package com.ssachtleben.play.plugin.auth.providers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.AuthUser;

/**
 * Provides basic provider functionality and allows to use it as Play {@link Plugin}.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseProvider<U extends AuthUser> extends Plugin {

	/**
	 * Contains all setting keys provided by application.conf.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class SettingKeys {
		public static final String SUCCESS = "success";
		public static final String ERROR = "error";
	}

	/**
	 * The static {@link Providers} instance contains all provider instances registered during application startup.
	 * 
	 * @author Sebastian Sachtleben
	 */
	@SuppressWarnings("rawtypes")
	public abstract static class Providers {
		private static final Logger.ALogger log = Logger.of(Providers.class);

		/**
		 * Keeps list of all registered {@link BaseProvider} instances.
		 */
		private static Map<String, BaseProvider> cache = new HashMap<String, BaseProvider>();

		/**
		 * Registers new {@link BaseProvider} for a specific key.
		 * 
		 * @param key
		 *          The provider key.
		 * @param provider
		 *          The {@link BaseProvider} instance.
		 */
		public static void register(final String key, final BaseProvider provider) {
			final Object previous = cache.put(key, provider);
			if (previous != null) {
				log.warn(String.format("There are multiple auth providers registered for key '%s'", key));
			}
		}

		/**
		 * Unregisters a {@link BaseProvider} for a specific key.
		 * 
		 * @param key
		 *          The provider key.
		 */
		public static void unregister(final String key) {
			cache.remove(key);
		}

		/**
		 * Returns instance of {@link BaseProvider} for a specific key.
		 * 
		 * @param key
		 *          The provider key.
		 * @return The {@link BaseProvider} instance.
		 */
		public static BaseProvider get(final String key) {
			return cache.get(key);
		}

		/**
		 * Checks if {@link BaseProvider} with specific key is registered.
		 * 
		 * @param key
		 *          The provider key.
		 * @return Success boolean.
		 */
		public static boolean has(final String name) {
			return cache.containsKey(name);
		}

		/**
		 * Provides a set of provider keys of all registered {@link BaseProvider} instances.
		 * 
		 * @return Set of provider keys.
		 */
		public static Set<String> keys() {
			return cache.keySet();
		}

		/**
		 * Provides the registered {@link BaseProvider} instances as collection.
		 * 
		 * @return Collection of registered {@link BaseProvider}.
		 */
		public static Collection<BaseProvider> list() {
			return cache.values();
		}
	}

	/**
	 * Keeps {@link Application} instance.
	 */
	private Application app;

	/**
	 * Default constructor for {@link BaseProvider} and will be invoked during application startup if the provider is registered as plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public BaseProvider(final Application app) throws MissingConfigurationException {
		this.app = app;
		validate();
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
	 * Handles authenticate process for this {@link BaseProvider}.
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @return Play {@link Result} object.
	 */
	public Result login(final Context ctx) {
		AuthUser authUser = handle(ctx);
		if (authUser != null) {
			Object user = Auth.service().find(authUser);
			logger().debug("User: " + user);
			if (user != null) {
				ctx.session().put(Auth.SESSION_USER_KEY, user.toString());
				return onSuccess(ctx);
			}
		}
		return onError(ctx);
	}

	/**
	 * Handles success behavior for this provider.
	 * 
	 * @param context
	 *          The current {@link Context}.
	 * @return Play {@link Result} object.
	 */
	protected Result onSuccess(final Context context) {
		return handleRedirect(config().getString(SettingKeys.SUCCESS), Results.ok());
	}

	/**
	 * Handles error behavior for this provider.
	 * 
	 * @param context
	 *          The current {@link Context}.
	 * @return Play {@link Result} object.
	 */
	protected Result onError(final Context context) {
		return handleRedirect(config().getString(SettingKeys.ERROR), Results.badRequest());
	}

	/**
	 * Handles redirect to url.
	 * 
	 * @param url
	 *          The url to set.
	 * @return Play {@link Result} object.
	 */
	protected Result handleRedirect(final String url, final Result result) {
		if (url != null && !"".equals(url)) {
			return Results.redirect(url);
		}
		return result;
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
	 * @return The {@link AuthUser}.
	 */
	protected abstract AuthUser handle(final Context context);

	/**
	 * List of setting keys which must be provided from the application.conf. These keys will be validated during application start.
	 * 
	 * @return List of setting keys.
	 */
	protected List<String> settingKeys() {
		return Collections.emptyList();
	}

	/**
	 * Validates {@link BaseProvider} registered as plugin during application start and checks if all mandatory setting keys provided by
	 * {@link #settingKeys()} are properly set in application.conf.
	 * 
	 * @throws MissingConfigurationException
	 *           The exception throws for missing setting keys in application.conf.
	 */
	private void validate() throws MissingConfigurationException {
		final List<String> settingKeys = settingKeys();
		if (settingKeys != null && settingKeys.size() >= 0) {
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
}