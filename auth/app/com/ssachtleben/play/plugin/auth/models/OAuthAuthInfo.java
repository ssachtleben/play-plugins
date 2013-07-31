package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

@SuppressWarnings("serial")
public class OAuthAuthInfo extends AuthInfo {

	private Token token;

	public OAuthAuthInfo(final Token token) {
		this.token = token;
	}

	public Token token() {
		return token;
	}
}
