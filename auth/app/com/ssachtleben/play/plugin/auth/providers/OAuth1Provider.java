package com.ssachtleben.play.plugin.auth.providers;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;

import play.Application;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Handle authentication process with oauth 1.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class OAuth1Provider<U extends OAuthAuthUser, I extends OAuthAuthInfo> extends OAuthProvider<U, I> {

	/**
	 * Creates new {@link OAuth1Provider} instance and validates setting keys in application.conf.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws Exception
	 */
	public OAuth1Provider(final Application app) throws Exception {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return service().getAuthorizationUrl(service().getRequestToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#login()
	 */
	@Override
	public Result login(final Context context) {
		logger().info("login oauth1 ...");
		final String oAuthToken = context.request().getQueryString("oauth_token");
		final String oAuthVerifier = context.request().getQueryString("oauth_verifier");
		if (StringUtils.isEmpty(oAuthToken) || StringUtils.isEmpty(oAuthVerifier)) {
			return Results.ok(authUrl());
		}
		final Token token = accessToken(oAuthToken, oAuthVerifier);
		logger().info("Found token: " + token.toString());
		final I info = info(token);
		logger().info("Found info: " + info.toString());
		final U user = transform(info);
		logger().info("Found identity: " + user.toString());
		Object obj = Auth.service().find(user);
		logger().info("User: " + obj);
		;
		if (obj != null) {
			context.session().put(Auth.SESSION_USER_KEY, "" + obj);
			String success = config().getString("success");
			return success == null || "".equals(success) ? Results.ok() : Results.redirect(success);
		}
		return Results.badRequest();
	}
}
