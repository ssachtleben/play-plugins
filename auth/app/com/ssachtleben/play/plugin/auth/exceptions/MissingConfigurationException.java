package com.ssachtleben.play.plugin.auth.exceptions;

/**
 * Throws if mandatory setting keys missing on provider plugin registration.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class MissingConfigurationException extends Exception {

  /**
   * Default constructor for {@link MissingConfigurationException}.
   * 
   * @param message
   *          The message to set.
   */
  public MissingConfigurationException(String message) {
    super(message);
  }
}
