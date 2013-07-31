package com.ssachtleben.play.plugin.auth.service;

import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.Identity;

/**
 * Provides an interface between the auth plugin and the application model layer. TODO: rework javadoc...
 * 
 * @author Sebastian Sachtleben
 */
public interface AuthService {

	Object find(final Identity identity);

	void save(final AuthUser authUser);

	void update(final AuthUser authUser);

}
