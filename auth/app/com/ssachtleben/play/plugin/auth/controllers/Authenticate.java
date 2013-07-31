package com.ssachtleben.play.plugin.auth.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import com.ssachtleben.play.plugin.auth.Auth;

/**
 * Provides login and logout methods to handle authentication process. TODO: rework javadoc...
 * 
 * @author Sebastian Sachtleben
 */
public class Authenticate extends Controller {

	public static Result login(final String provider) {
		return Auth.login(ctx(), provider);
	}

	public static Result logout() {
		return Auth.logout(session());
	}

}
