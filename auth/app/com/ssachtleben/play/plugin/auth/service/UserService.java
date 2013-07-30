package com.ssachtleben.play.plugin.auth.service;

import com.ssachtleben.play.plugin.auth.models.AuthUser;

/**
 * Provides an interface between the auth plugin and the application model layer. TODO: rework javadoc...
 * 
 * @author Sebastian Sachtleben
 */
public interface UserService {

	public void save(final AuthUser authUser);

	public void update(final AuthUser authUser);

}
