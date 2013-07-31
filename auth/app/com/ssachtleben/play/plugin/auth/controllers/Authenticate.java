package com.ssachtleben.play.plugin.auth.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;

/**
 * Provides login and logout methods to handle authentication process via this plugin. The {@link #login(String)} and {@link #logout()}
 * methods can directly linked in the routes configuration. But if you need additional code in this routes like jpa transaction, then you
 * can create your own routes and use {@link Auth#login(play.mvc.Http.Context, String)} and {@link Auth#logout(play.mvc.Http.Session)}
 * directly.
 * 
 * @author Sebastian Sachtleben
 */
public class Authenticate extends Controller {

	/**
	 * Tries to login the current user with the given {@code provider}.
	 * 
	 * @param provider
	 *          The provider key.
	 * @return Play {@link Result} object.
	 */
	public static Result login(final String provider) {
		return Auth.login(ctx(), provider);
	}

	/**
	 * Logout the current logged in user.
	 * 
	 * @return Play {@link Result} object.
	 */
	public static Result logout() {
		return Auth.logout(session());
	}
}
