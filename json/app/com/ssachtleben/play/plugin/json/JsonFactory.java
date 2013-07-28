package com.ssachtleben.play.plugin.json;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Converts any kind of {@link Object} or {@link Collection} to {@link String}
 * or {@link JsonNode}.
 * <p>
 * Use {link {@link #instance()} to retrieve a instance of {@link JsonFactory}.
 * For adding new mixins just chain to {@link #with(Class...)}. Return the
 * string representation use {@link #string(Object...)} and for the
 * {@link JsonNode} use {{@link #json(Object...)}.
 * </p>
 * <p>
 * Example:
 * {@code JsonFactory.instance().with(Account.class, AccountMixin.class).json(obj)}
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
public class JsonFactory {

  /**
   * Provides the {@link ObjectMapper} to serialize or deserialize json.
   */
  private ObjectMapper mapper;

  /**
   * Contains all mixin classes.
   */
  private Map<Class<?>, Class<?>> mixins;

  /**
   * Provides new factory instance. Use {@link #instance()} to fetch the current
   * instance of {@link JsonFactory}.
   */
  private JsonFactory() {
    this.mapper = new ObjectMapper();
    this.mixins = new HashMap<Class<?>, Class<?>>();
  }

  /**
   * Returns new instance of {@link JsonFactory}.
   * 
   * @return The {@link JsonFactory} instance.
   */
  public static JsonFactory instance() {
    return new JsonFactory();
  }

  // --- CHAIN CONFIG METHODS ---

  /**
   * Adds mixins to current {@link ObjectMapper} instance.
   * 
   * @param mixins
   *          The mixins to add.
   * @return The {@link JsonFactory} instance.
   */
  public JsonFactory with(Class<?>... mixins) {
    for (int i = 0; i < mixins.length; i += 2) {
      this.mixins.put(mixins[i], mixins[i + 1]);
    }
    return this;
  }

  // --- OUTPUT METHODS ---

  /**
   * Converts the {@code objects} to string.
   * <p>
   * Uses the {@link ObjectMapper} to convert the {@code objects} to string.
   * Mixins can added via {@link #with(Class...)}.
   * </p>
   * 
   * @param objects
   *          The objects to convert.
   * @return The converted json string.
   * @throws JsonGenerationException
   * @throws JsonMappingException
   * @throws IOException
   */
  public String string(Object... objects) throws JsonGenerationException, JsonMappingException, IOException {
    addMixins();
    return this.mapper.writeValueAsString(objects);
  }

  /**
   * Converts the {@code objects} to {@link JsonNode} and returns the node.
   * <p>
   * Uses the {@link #string(Object...)} method to convert {@code objects} to
   * string and read the tree with {@link ObjectMapper#readTree(String)} to
   * return the json node.
   * </p>
   * <p>
   * The {@link ObjectMapper} can't convert the {@code objects} directly to
   * {@link JsonNode}. Only {@link ObjectMapper#writeValueAsString(Object)}
   * supports mixins.
   * </p>
   * 
   * @param objects
   *          The objects to convert.
   * @return The converted {@link JsonNode}.
   * @throws JsonGenerationException
   * @throws JsonMappingException
   * @throws IOException
   */
  public JsonNode json(Object... objects) throws JsonGenerationException, JsonMappingException, IOException {
    return this.mapper.readTree(string(objects));
  }

  // --- PRIVATE METHODS ---

  /**
   * Add the given mixins via {@link #with(Class...)} to the
   * {@link ObjectMapper}.
   */
  private void addMixins() {
    for (Class<?> c : this.mixins.keySet()) {
      this.mapper.getSerializationConfig().addMixInAnnotations(c, this.mixins.get(c));
    }
  }
}
