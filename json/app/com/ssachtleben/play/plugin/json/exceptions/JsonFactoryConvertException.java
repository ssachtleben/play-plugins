package com.ssachtleben.play.plugin.json.exceptions;

import com.ssachtleben.play.plugin.json.JsonFactory;

/**
 * Throws on exception during the {@link ObjectMapper} convert process in
 * {@link JsonFactory}. This is a wrapper for any other exception.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class JsonFactoryConvertException extends Exception {

  /**
   * Default {@link JsonFactoryConvertException} constructor with an exception.
   * 
   * @param e
   *          The ocurred exception.
   */
  public JsonFactoryConvertException(Exception e) {
    super(e);
  }

}
