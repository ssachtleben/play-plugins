package com.ssachtleben.play.plugin.auth;

import play.Application;
import play.Configuration;
import play.Play;
import play.mvc.Http.Session;

import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;

/**
 * Provides the logged in user and additional methods. TODO: rework javadoc...
 * 
 * @author Sebastian Sachtleben
 */
public class Authenticator {

	public static final String SESSION_USER_KEY = "u";
	public static final String SETTING_KEY_AUTH = "auth";

	/**
	 * Returns auth configuration from application conf file.
	 * 
	 * @return The {@link Configuration} instance.
	 * @throws MissingConfigurationException
	 */
	public static Configuration config() throws MissingConfigurationException {
		return config(Play.application());
	}

	/**
	 * Returns auth configuration from application conf file.
	 * 
	 * @param app
	 *          The play {@link Application} instance.
	 * @return The {@link Configuration} instance.
	 * @throws MissingConfigurationException
	 */
	public static Configuration config(Application app) throws MissingConfigurationException {
		final Configuration config = app.configuration();
		final Configuration authConfig = config.getConfig(SETTING_KEY_AUTH);
		if (authConfig == null) {
			throw new MissingConfigurationException(String.format("Missing setting key '%s' in application.conf", SETTING_KEY_AUTH));
		}
		return authConfig;
	}

	/**
	 * Check if the current user is logged in.
	 * 
	 * @param session
	 *          Current {@link Session} instance.
	 * @return The success boolean.
	 */
	public static boolean isLoggedIn(final Session session) {
		return session.containsKey(SESSION_USER_KEY);
	}

	/**
	 * Logs the current logged in user out.
	 * 
	 * @param session
	 *          Current {@link Session} instance.
	 */
	public static void logout(final Session session) {
		session.remove(SESSION_USER_KEY);
	}

	/**
	 * Logs the current request in.
	 * 
	 * @param provider
	 *          The login provider.
	 */
	public static void login(final String provider) {
		// TODO: handle login...
	}

}
