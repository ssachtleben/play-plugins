package com.ssachtleben.play.plugin.auth.providers;

import java.util.Map;

import play.Application;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.EmailPasswordAuthUser;

/**
 * Provides authentication with email and password.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = EmailPasswordAuthUser.class)
public class EmailPassword extends BaseProvider<EmailPasswordAuthUser> {
	
	/**
	 * The unique provider name for {@link EmailPassword} provider.
	 */
	public static final String KEY = "email";

	/**
	 * Default constructor for {@link EmailPassword} provider and will be invoked during application startup if the provider is registered as
	 * plugin.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 * @throws MissingConfigurationException
	 *           The exception will be thrown for missing mandatory setting keys.
	 */
	public EmailPassword(Application app) throws MissingConfigurationException {
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
	 * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#handle(play.mvc.Http.Context)
	 */
	@Override
	protected AuthUser handle(Context context) {
		Map<String, String[]> params = context.request().body().asFormUrlEncoded();
		if (params != null && params.containsKey("email") && params.containsKey("password")) {
			return new EmailPasswordAuthUser(params.get("email")[0], params.get("password")[0]);
		}
		return null;
	}
}
