package com.ssachtleben.play.plugin.auth.providers.oauth2;

import play.Application;
import play.mvc.Http.Context;

import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

/**
 * Provides authentication with Google oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
public class Google extends BaseProvider {

	public Google(Application app) {
		super(app);
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object authenticate(Context context, Object payload) {
		// TODO Auto-generated method stub
		return null;
	}
}
