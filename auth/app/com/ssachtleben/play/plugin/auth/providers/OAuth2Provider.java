package com.ssachtleben.play.plugin.auth.providers;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

import play.Application;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.OAuthAuthUser;

/**
 * Handle authentication process with oauth 2.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class OAuth2Provider<U extends OAuthAuthUser, I extends OAuthAuthInfo> extends OAuthProvider<U, I> {

	/**
	 * Creates new {@link OAuth2Provider} instance and validates setting keys in application.conf.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws Exception
	 */
	public OAuth2Provider(Application app) throws Exception {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#authUrl()
	 */
	@Override
	public String authUrl() {
		return service().getAuthorizationUrl(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#login()
	 */
	@Override
	public Result login(final Context context) {
		logger().info("login oauth2 ...");
		String code = context.request().getQueryString("code");
		if (StringUtils.isEmpty(code)) {
			return Results.ok(authUrl());
		}
		Token token = service().getAccessToken(null, new Verifier(code));
		logger().info("Found token: " + token.toString());
		final I info = info(token);
		logger().info("Found info: " + info.toString());
		final U user = transform(info);
		logger().info("Found identity: " + user.toString());
		Object obj = Auth.service().find(user);
		logger().info("User: " + obj);
		if (obj != null) {
			context.session().put(Auth.SESSION_USER_KEY, "" + obj);
			return Results.ok(popup());
		}
		return Results.badRequest();
	}
}