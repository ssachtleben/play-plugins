package com.ssachtleben.play.plugin.auth.service;

import play.Application;
import play.Logger;
import play.Plugin;

import com.ssachtleben.play.plugin.auth.Auth;

/**
 * Provides the interface between auth plugin and application model layer.
 * <p>
 * The application needs a registered {@link Plugin} which extends
 * {@link AuthServicePlugin} in order to provide authentication functionality.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AuthServicePlugin extends Plugin implements AuthService {

  /**
   * The logger for {@link AuthServicePlugin} class.
   */
  private static final Logger.ALogger log = Logger.of(AuthServicePlugin.class);

  /**
   * Keeps {@link Application} instance.
   */
  protected Application app;

  /**
   * Default contructor for {@link AuthServicePlugin} and will be invoked during
   * application startup.
   * 
   * @param app
   *          The {@link Application} instance.
   */
  public AuthServicePlugin(final Application app) {
    this.app = app;
  }

  /**
   * Provides the current {@link Application} instance.
   * 
   * @return The {@link Application} instance.
   */
  protected Application app() {
    return app;
  }

  /*
   * (non-Javadoc)
   * 
   * @see play.Plugin#onStart()
   */
  @Override
  public void onStart() {
    if (Auth.hasService()) {
      log.warn("A auth service was already registered - replacing the old one, however this might hint to a configuration problem if this is a production environment.");
    }
    Auth.service(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see play.Plugin#onStop()
   */
  @Override
  public void onStop() {
    Auth.service(null);
  }
}
