package com.ssachtleben.play.plugin.json.exceptions;

import org.codehaus.jackson.map.ObjectMapper;

import com.ssachtleben.play.plugin.json.JsonFactory;

/**
 * Throws on exception during the {@link ObjectMapper} convert process in
 * {@link JsonFactory}. This is a wrapper for any other exception.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class JsonFactoryConvertException extends Exception {

  public JsonFactoryConvertException(Exception e) {
    super(e);
  }

}
