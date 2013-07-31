package com.ssachtleben.play.plugin.auth.providers;

import java.util.Map;

import play.Application;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.models.AuthInfo;
import com.ssachtleben.play.plugin.auth.models.EmailPasswordAuthUser;

/**
 * Provides authentication with email and password.
 * 
 * @author Sebastian Sachtleben
 */
public class EmailPassword extends BaseProvider<EmailPasswordAuthUser, AuthInfo> {
	public static final String KEY = "email";

	public EmailPassword(Application app) {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#key()
	 */
	@Override
	public String key() {
		return KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#login(play.mvc.Http.Context)
	 */
	@Override
	public Result login(Context context) {
		Map<String, String[]> params = context.request().body().asFormUrlEncoded();
		if (params != null && params.containsKey("email") && params.containsKey("password")) {
			String email = params.get("email")[0];
			String password = params.get("password")[0];
			EmailPasswordAuthUser user = new EmailPasswordAuthUser(email, password);
			logger().info("Found identity: " + user.toString());
			Object obj = Auth.service().find(user);
			logger().info("User: " + obj);
			if (obj != null) {
				context.session().put(Auth.SESSION_USER_KEY, "" + obj);
				String success = config().getString("success");
				return success == null || "".equals(success) ? Results.ok() : Results.redirect(success);
			}
		}
		return Results.badRequest();
	}
}
