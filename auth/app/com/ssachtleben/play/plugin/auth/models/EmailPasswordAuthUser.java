package com.ssachtleben.play.plugin.auth.models;

import com.ssachtleben.play.plugin.auth.providers.EmailPassword;

@SuppressWarnings("serial")
public class EmailPasswordAuthUser extends AuthUser {

	private String email;
	private String clearPassword;

	public EmailPasswordAuthUser(final String email, final String clearPassword) {
		this.email = email;
		this.clearPassword = clearPassword;
	}

	@Override
	public String id() {
		return email;
	}

	@Override
	public String provider() {
		return EmailPassword.KEY;
	}

	public String clearPassword() {
		return clearPassword;
	}
}
