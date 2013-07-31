package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Google;

@SuppressWarnings("serial")
public class GoogleAuthUser extends OAuthAuthUser {

	public GoogleAuthUser(String id, Token token) {
		super(id, token);
	}

	@Override
	public String provider() {
		return Google.KEY;
	}
}
