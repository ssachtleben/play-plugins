package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Twitter;

@SuppressWarnings("serial")
public class TwitterAuthUser extends OAuthAuthUser {

	public TwitterAuthUser(final Token token) {
		super(userId(token), token);
	}

	@Override
	public String provider() {
		return Twitter.KEY;
	}

	private static String userId(Token token) {
		String user_id = null;
		String tmp = token.getRawResponse();
		String[] array = tmp.split("&");

		for (int i = 0; i < array.length; i++) {
			if (array[i].startsWith("user_id")) {
				user_id = (array[i].split("="))[1];
			}
		}
		return user_id;
	}
}
