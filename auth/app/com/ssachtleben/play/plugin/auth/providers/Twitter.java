package com.ssachtleben.play.plugin.auth.providers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi.Authenticate;
import org.scribe.model.Token;

import play.Application;
import play.api.templates.Html;

import com.ssachtleben.play.plugin.auth.models.OAuthAuthInfo;
import com.ssachtleben.play.plugin.auth.models.TwitterAuthUser;

/**
 * Provides authentication with Twitter oauth1 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Twitter extends OAuth1Provider<TwitterAuthUser, OAuthAuthInfo> {
	public static final String KEY = "twitter";

	public Twitter(Application app) throws Exception {
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
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#provider()
	 */
	@Override
	public Class<? extends Api> provider() {
		return Authenticate.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuth1Provider#info(org.scribe.model.Token)
	 */
	@Override
	protected OAuthAuthInfo info(Token token) {
		// TODO: Read data from twitter for creating new account or comparing current values.
		return new OAuthAuthInfo(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuth1Provider#transform(com.ssachtleben.play.plugin.auth.models.OAuth1AuthInfo)
	 */
	@Override
	protected TwitterAuthUser transform(OAuthAuthInfo info) {
		return new TwitterAuthUser(info.token());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#popup()
	 */
	@Override
	protected Html popup() {
		return views.html.popups.twitter.render();
	}
}
