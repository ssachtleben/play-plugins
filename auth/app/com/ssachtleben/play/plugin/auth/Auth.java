package com.ssachtleben.play.plugin.auth;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Play;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.providers.BaseProvider.Providers;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * Provides the logged in user and additional methods. TODO: rework javadoc...
 * 
 * @author Sebastian Sachtleben
 */
public class Auth {
	private static final Logger.ALogger log = Logger.of(Auth.class);

	/**
	 * The session cookie key to identify authenticated identities.
	 */
	protected static final String SESSION_USER_KEY = "u";

	/**
	 * The setting key for all auth configuration properties in application.cof.
	 */
	protected static final String SETTING_KEY_AUTH = "auth";

	private static AuthService authService;

	/**
	 * Logs the current request in.
	 * 
	 * @param ctx
	 *          The {@link Context} to set.
	 * @param provider
	 *          The provider to set.
	 * @return Play {@link Result} object.
	 */
	public static Result login(final Context ctx, final String provider) {
		log.info(String.format("Login %s", provider));
		if (!Providers.has(provider)) {
			log.warn(String.format("Provider '%s' is unknowned", provider));
			return Results.notFound();
		}
		return Providers.get(provider).login(ctx);
	}

	/**
	 * Logs the current logged in user out.
	 * 
	 * @param session
	 *          Current {@link Session} instance.
	 * @return Play {@link Result} object.
	 */
	public static Result logout(final Session session) {
		if (isLoggedIn(session)) {
			log.info(String.format("Logout %s successfully", getLoggedIn(session)));
			session.remove(SESSION_USER_KEY);
		}
		return Results.ok();
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
	 * Get logged in user.
	 * 
	 * @param session
	 *          Current {@link Session} instance.
	 * @return The string from session.
	 */
	public static String getLoggedIn(final Session session) {
		return session.get(SESSION_USER_KEY);
	}

	/**
	 * Sets given {@link AuthService} as active service.
	 * 
	 * @param service
	 *          The {@link AuthService} to set.
	 */
	public static void service(final AuthService service) {
		authService = service;
	}

	/**
	 * Returns the current {@link AuthService} instance.
	 * 
	 * @return The {@link AuthService} instance.
	 */
	public static AuthService service() {
		if (authService == null) {
			throw new RuntimeException("AuthService plugin not found ... Please check documentation!");
		}
		return authService;
	}

	/**
	 * Checks if {@link AuthService} is registered.
	 * 
	 * @return The success boolean.
	 */
	public static boolean hasService() {
		return authService != null;
	}

	/**
	 * Returns auth configuration from application conf file.
	 * 
	 * @return The {@link Configuration} instance.
	 */
	public static Configuration config() {
		return config(Play.application());
	}

	/**
	 * Returns auth configuration from application conf file.
	 * 
	 * @param app
	 *          The play {@link Application} instance.
	 * @return The {@link Configuration} instance.
	 */
	public static Configuration config(Application app) {
		final Configuration config = app.configuration();
		if (config != null) {
			return config.getConfig(SETTING_KEY_AUTH);
		}
		return null;
	}
}
