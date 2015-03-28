package com.ssachtleben.play.plugin.auth.models;

/**
 * Provides accessible id and provider for authenticated users.
 * 
 * @author Sebastian Sachtleben
 */
public interface Identity {

  /**
   * The user id of the identity.
   * 
   * @return The user id.
   */
  String id();

  /**
   * The provider used during login, for example email, facebook, twitter etc.
   * 
   * @return The provider.
   */
  String provider();

}
