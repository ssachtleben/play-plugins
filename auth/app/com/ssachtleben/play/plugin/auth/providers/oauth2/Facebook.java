package com.ssachtleben.play.plugin.auth.providers.oauth2;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;

import play.Application;

import com.ssachtleben.play.plugin.auth.providers.OAuthProvider;

/**
 * Provides authentication with Facebook oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Facebook extends OAuthProvider {

	public Facebook(Application app) throws Exception {
		super(app);
	}

	@Override
	public String key() {
		return "facebook";
	}

	@Override
	public Class<? extends Api> provider() {
		return FacebookApi.class;
	}
}
