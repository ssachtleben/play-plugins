package com.ssachtleben.play.plugin.auth;

import play.Configuration;
import play.Play;
import play.mvc.Http.Session;

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
	 */
	public static Configuration config() {
		return Play.application().configuration().getConfig(SETTING_KEY_AUTH);
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
