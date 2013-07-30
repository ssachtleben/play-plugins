package com.ssachtleben.play.plugin.auth.providers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.GoogleApi;

import play.Application;


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
