package com.ssachtleben.play.plugin.auth.providers.oauth1;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi.Authenticate;

import play.Application;

import com.ssachtleben.play.plugin.auth.providers.OAuthProvider;

/**
 * Provides authentication with Twitter oauth1 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Twitter extends OAuthProvider {

	public Twitter(Application app) throws Exception {
		super(app);
	}

	@Override
	public String key() {
		return "twitter";
	}

	@Override
	public Class<? extends Api> provider() {
		return Authenticate.class;
	}
}
