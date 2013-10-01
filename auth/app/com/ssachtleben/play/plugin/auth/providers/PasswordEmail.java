package com.ssachtleben.play.plugin.auth.providers;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.PasswordEmailAuthUser;

/**
 * Provides authentication with email and password.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = PasswordEmailAuthUser.class)
public class PasswordEmail extends BaseProvider<PasswordEmailAuthUser> {

	/**
	 * The unique provider name for {@link PasswordEmail} provider.
	 */
	public static final String KEY = "email";

	/**
	 * Contains all request parameter names.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static abstract class RequestParameter {
		public static final String EMAIL = "email";
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
		String contentType = context.request().getHeader("content-type");
		if ("application/json".equals(contentType)) {
			JsonNode node = context.request().body().asJson();
			return new PasswordEmailAuthUser(node.get(RequestParameter.EMAIL).asText(), node.get(RequestParameter.PASSWORD).asText(),
					node);
		} else {
			Map<String, String[]> params = context.request().body().asFormUrlEncoded();
			if (params != null && params.containsKey(RequestParameter.EMAIL) && params.containsKey(RequestParameter.PASSWORD)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode data;
				try {
					data = mapper.readTree(mapper.writeValueAsString(params));
				} catch (IOException e) {
					logger().error("Failed to serialize params to json", e);
					data = mapper.createObjectNode();
				}
				return new PasswordEmailAuthUser(params.get(RequestParameter.EMAIL)[0], params.get(RequestParameter.PASSWORD)[0], data);
			}
		}
		return null;
	}
}
