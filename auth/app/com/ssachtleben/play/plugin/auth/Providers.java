package com.ssachtleben.play.plugin.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;

import play.Logger;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

/**
 * Provides a list of registered {@link BaseProvider} providers and allow to register and unregister them.
 * 
 * @author Sebastian Sachtleben
 */
public class Providers {

	private static final Logger.ALogger log = Logger.of(Providers.class);

	/**
	 * Keeps list of all registered {@link BaseProvider} instances.
	 */
	private static Map<String, BaseProvider<?>> cache = new HashMap<String, BaseProvider<?>>();

	/**
	 * Registers new {@link BaseProvider} for a specific key.
	 * 
	 * @param key
	 *          The provider key.
	 * @param provider
	 *          The {@link BaseProvider} instance.
	 */
	public static void register(final String key, final BaseProvider<?> provider) {
		log.info(String.format("Register %s provider", WordUtils.capitalize(key)));
		try {
			provider.validate();
			final Object previous = cache.put(key, provider);
			if (previous != null) {
				log.warn(String.format("There are multiple auth providers registered for key '%s'", key));
			}
		} catch (MissingConfigurationException e) {
			log.error(String.format("Failed to register %s provider", key), e);
		}

	}

	/**
	 * Unregisters a {@link BaseProvider} for a specific key.
	 * 
	 * @param key
	 *          The provider key.
	 */
	public static void unregister(final String key) {
		log.info(String.format("Unregister %s provider", key));
		cache.remove(key);
	}

	/**
	 * Returns instance of {@link BaseProvider} for a specific key.
	 * 
	 * @param key
	 *          The provider key.
	 * @return The {@link BaseProvider} instance.
	 */
	public static BaseProvider<?> get(final String key) {
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
	public static Collection<BaseProvider<?>> list() {
		return cache.values();
	}

}
