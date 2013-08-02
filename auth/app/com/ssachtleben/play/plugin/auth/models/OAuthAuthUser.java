package com.ssachtleben.play.plugin.auth.models;

import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.BaseOAuthProvider;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The abstract {@link OAuthAuthUser} contains {@code id} and {@code token} properties.
 * <p>
 * All oauth provider uses this model which extend from this and pass it to {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see BaseOAuthProvider
 */
@SuppressWarnings("serial")
public abstract class OAuthAuthUser extends AuthUser {

	/**
	 * Keeps the id used during authentication process.
	 */
	private String id;

	/**
	 * Keeps the token used during authentication process.
	 */
	private Token token;

	/**
	 * Default constructor for {@link OAuthAuthUser}.
	 * 
	 * @param id
	 *          The id to set
	 * @param token
	 *          The token to set
	 */
	public OAuthAuthUser(final String id, final Token token) {
		this.id = id;
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#id()
	 */
	@Override
	public String id() {
		return id;
	}

	/**
	 * The request token used during the authentication process.
	 * 
	 * @return The {@link Token}.
	 */
	public Token token() {
		return token;
	}
}
