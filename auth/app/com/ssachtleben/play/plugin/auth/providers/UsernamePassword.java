package com.ssachtleben.play.plugin.auth.providers;

import java.util.Map;

import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.UsernamePasswordAuthUser;

/**
 * Provides authentication with username and password.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = UsernamePasswordAuthUser.class)
public class UsernamePassword extends BaseProvider<UsernamePasswordAuthUser> {

	/**
	 * The unique provider name for {@link UsernamePassword} provider.
	 */
	public static final String KEY = "username";

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
		if (params != null && params.containsKey("username") && params.containsKey("password")) {
			return new UsernamePasswordAuthUser(params.get("username")[0], params.get("password")[0]);
		}
		return null;
	}
}
