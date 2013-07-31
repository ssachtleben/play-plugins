package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

@SuppressWarnings("serial")
public abstract class OAuthAuthUser extends AuthUser {

	private String id;
	private Token token;

	public OAuthAuthUser(final String id, final Token token) {
		this.id = id;
		this.token = token;
	}

	@Override
	public String id() {
		return id;
	}

	public Token token() {
		return token;
	}
}
