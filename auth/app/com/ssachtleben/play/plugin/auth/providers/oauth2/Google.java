package com.ssachtleben.play.plugin.auth.providers.oauth2;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;

import play.Application;

import com.ssachtleben.play.plugin.auth.providers.OAuthProvider;

/**
 * Provides authentication with Google oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Google extends OAuthProvider {

	public Google(Application app) throws Exception {
		super(app);
	}

	@Override
	public String key() {
		return "google";
	}

	@Override
	public Class<? extends Api> provider() {
		return GoogleApi.class;
	}
}
