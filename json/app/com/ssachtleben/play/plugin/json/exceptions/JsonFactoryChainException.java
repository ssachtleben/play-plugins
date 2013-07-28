package com.ssachtleben.play.plugin.json.exceptions;

import com.ssachtleben.play.plugin.json.JsonFactory;

/**
 * Indicates a wrong usage of the chaining functionality of {@link JsonFactory}.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class JsonFactoryChainException extends Exception {

  /**
   * Default {@link JsonFactoryChainException} constructor with message that
   * contains what happend.
   * 
   * @param message
   *          The message about the problem.
   */
  public JsonFactoryChainException(String message) {
    super(message);
  }

  /**
   * Default {@link JsonFactoryChainException} constructor with an exception.
   * 
   * @param e
   *          The ocurred exception.
   */
  public JsonFactoryChainException(Exception e) {
    super(e);
  }
}
