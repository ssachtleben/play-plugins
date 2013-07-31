package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;

@SuppressWarnings("serial")
public class FacebookAuthUser extends OAuthAuthUser {

	public FacebookAuthUser(String id, Token token) {
		super(id, token);
	}

	@Override
	public String provider() {
		return Facebook.KEY;
	}
}
