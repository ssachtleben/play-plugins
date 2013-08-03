package com.ssachtleben.play.plugin.auth.providers;

import java.util.Map;

import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.PasswordUsernameAuthUser;

/**
 * Provides authentication with username and password.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = PasswordUsernameAuthUser.class)
public class PasswordUsername extends BaseProvider<PasswordUsernameAuthUser> {

	/**
	 * The unique provider name for {@link PasswordUsername} provider.
	 */
	public static final String KEY = "username";

	/**
	 * Contains all request parameter names.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class RequestParameter {
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
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
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#handle(play.mvc.Http.Context)
	 */
	@Override
	protected AuthUser handle(Context context) {
		Map<String, String[]> params = context.request().body().asFormUrlEncoded();
		if (params != null && params.containsKey(RequestParameter.USERNAME) && params.containsKey(RequestParameter.PASSWORD)) {
			return new PasswordUsernameAuthUser(params.get(RequestParameter.USERNAME)[0], params.get(RequestParameter.PASSWORD)[0]);
		}
		return null;
	}
}
