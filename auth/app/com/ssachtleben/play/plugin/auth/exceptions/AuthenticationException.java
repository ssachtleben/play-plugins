package com.ssachtleben.play.plugin.auth.exceptions;

/**
 * Throws if a problem occur during authentication process.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {

  /**
   * Default constructor for {@link AuthenticationException}.
   * 
   * @param message
   *          The message to set.
   */
  public AuthenticationException(String message) {
    super(message);
  }

}
