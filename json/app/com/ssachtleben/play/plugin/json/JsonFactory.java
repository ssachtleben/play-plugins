package com.ssachtleben.play.plugin.json;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.ssachtleben.play.plugin.json.exceptions.JsonFactoryChainException;
import com.ssachtleben.play.plugin.json.exceptions.JsonFactoryConvertException;

/**
 * Converts any kind of {@link Object} or {@link Collection} to {@link String}
 * or {@link JsonNode}.
 * <p>
 * Use {@link #instance()} to retrieve a instance of {@link JsonFactory}. Its
 * important to add convertible objects via {@link #convert(Object)} or
 * {@link #convert(String, Object)}.
 * </p>
 * <p>
 * For adding new mixins just chain to {@link #with(Class...)}. Return the
 * string representation use {@link #string(Object...)} and for the
 * {@link JsonNode} use { {@link #json(Object...)}.
 * </p>
 * <p>
 * Example:
 * {@code JsonFactory.instance().convert(acc).with(Account.class, AccountMixin.class).json()}
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
public class JsonFactory {

  /**
   * Provides the {@link ObjectMapper} to serialize or deserialize json.
   */
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Contains last added object via {@link #convert(Object)} or
   * {@link #convert(String, Object)}.
   */
  private Object lastAddedObject;

  /**
   * Contains last added path via {@link #convert(String, Object)}.
   */
  private String lastAddedPath;

  /**
   * Contains all mixin classes.
   */
  private Map<Class<?>, Class<?>> mixins = new HashMap<Class<?>, Class<?>>();

  /**
   * Contains the objects to convert for specific paths.
   */
  private Map<String, Object> content = new HashMap<String, Object>();

  /**
   * Contains all mixin classes for specific paths.
   */
  private Map<String, Map<Class<?>, Class<?>>> pathMixins = new HashMap<String, Map<Class<?>, Class<?>>>();

  /**
   * Returns new instance of {@link JsonFactory}.
   * 
   * @return The {@link JsonFactory} instance.
   */
  public static JsonFactory instance() {
    return new JsonFactory();
  }

  // --- CHAIN CONFIGURATION METHODS ---

  /**
   * Add new object to the current factory.
   * 
   * @param object
   *          The object to add.
   * @return The {@link JsonFactory} instance.
   */
  public JsonFactory convert(Object object) {
    this.lastAddedObject = object;
    return this;
  }

  /**
   * Add {@code object} to a specific {@code path}.
   * 
   * @param path
   *          The path to set.
   * @param object
   *          The object to add.
   * @return The {@link JsonFactory} instance.
   */
  public JsonFactory convert(String path, Object object) {
    this.lastAddedPath = path;
    this.lastAddedObject = object;
    return this;
  }

  /**
   * Adds mixins provided by an {@link MixinProvider} class to the last added
   * object via {@link #convert(Object)} or {@link #convert(String, Object)}.
   * 
   * @param providerClass
   *          The providerClass needs to implements the {@link MixinProvider}
   *          interface.
   * @return The {@link JsonFactory} instance.
   * @throws JsonFactoryChainException
   *           This exception occur if the {@code providerClass} not implements
   *           the {@link MixinProvider} interface or no convertible objects
   *           provided before via {@link #convert(Object)} or
   *           {@link #convert(String, Object)}.
   */
  public JsonFactory with(Class<? extends MixinProvider> providerClass) throws JsonFactoryChainException {
    try {
      return with(providerClass.newInstance());
    } catch (InstantiationException e) {
      throw new JsonFactoryChainException(e);
    } catch (IllegalAccessException e) {
      throw new JsonFactoryChainException(e);
    }
  }

  /**
   * Adds mixins provided by {@link MixinProvider} to the last added object via
   * {@link #convert(Object)} or {@link #convert(String, Object)}.
   * 
   * @param provider
   *          The provider to set.
   * @return The {@link JsonFactory} instance.
   * @throws JsonFactoryChainException
   *           This exception occur if no convertible objects provided before
   *           via {@link #convert(Object)} or {@link #convert(String, Object)}.
   */
  public JsonFactory with(MixinProvider provider) throws JsonFactoryChainException {
    return with(provider.mixins());
  }

  /**
   * Adds mixins to the last added object via {@link #convert(Object)} or
   * {@link #convert(String, Object)}.
   * 
   * @param mixins
   *          The mixins to add.
   * @return The {@link JsonFactory} instance.
   * @throws JsonFactoryChainException
   *           This exception occur if no convertible objects provided before
   *           via {@link #convert(Object)} or {@link #convert(String, Object)}.
   */
  public JsonFactory with(Class<?>... mixins) throws JsonFactoryChainException {
    if (this.lastAddedObject == null) {
      throw new JsonFactoryChainException("Use the convert() method to provide convertable content before adding mixins");
    }
    if (this.lastAddedPath != null) {
      if (this.pathMixins.containsKey(this.lastAddedPath)) {
        throw new JsonFactoryChainException(String.format("Found registered mixins for path '%s'", this.lastAddedPath));
      }
      this.pathMixins.put(this.lastAddedPath, new HashMap<Class<?>, Class<?>>());
      for (int i = 0; i < mixins.length; i += 2) {
        this.pathMixins.get(this.lastAddedPath).put(mixins[i], mixins[i + 1]);
      }
    } else {
      for (int i = 0; i < mixins.length; i += 2) {
        this.mixins.put(mixins[i], mixins[i + 1]);
      }
    }
    return this;
  }

  // --- OUTPUT METHODS ---

  /**
   * Converts objects added via {@link #convert(Object)} or
   * {@link #convert(String, Object)} to {@link String}.
   * 
   * @throws JsonFactoryConvertException
   *           Throws if exception occur during convert process.
   * @throws JsonFactoryChainException
   *           This exception occur if no convertible objects provided before
   *           via {@link #convert(Object)} or {@link #convert(String, Object)}.
   */
  public String string() throws JsonFactoryConvertException, JsonFactoryChainException {
    try {
      return this.mapper.writeValueAsString(createObjectNode());
    } catch (JsonGenerationException e) {
      throw new JsonFactoryConvertException(e);
    } catch (JsonMappingException e) {
      throw new JsonFactoryConvertException(e);
    } catch (IOException e) {
      throw new JsonFactoryConvertException(e);
    }
  }

  /**
   * Converts objects added via {@link #convert(Object)} or
   * {@link #convert(String, Object)} to {@link JsonNode}.
   * 
   * @throws JsonFactoryConvertException
   *           Throws if exception occur during convert process.
   * @throws JsonFactoryChainException
   *           This exception occur if no convertible objects provided before
   *           via {@link #convert(Object)} or {@link #convert(String, Object)}.
   */
  public JsonNode json() throws JsonFactoryConvertException, JsonFactoryChainException {
    return createObjectNode();
  }

  // --- PRIVATE METHODS ---

  /**
   * Converts the objects added via {@link #convert(Object)} or
   * {@link #convert(String, Object)} to {@link JsonNode}.
   * 
   * @return The converted {@link JsonNode}.
   * @throws JsonFactoryConvertException
   *           Throws if exception occur during convert process.
   * @throws JsonFactoryChainException
   *           This exception occur if no convertible objects provided before
   *           via {@link #convert(Object)} or {@link #convert(String, Object)}.
   */
  private JsonNode createObjectNode() throws JsonFactoryConvertException, JsonFactoryChainException {
    if (this.lastAddedObject == null) {
      throw new JsonFactoryChainException("Use the convert() method to provide convertable content before convert to string");
    }
    try {
      return this.content.isEmpty() ? createSimpleObjectNode() : createPathObjectNode();
    } catch (JsonGenerationException e) {
      throw new JsonFactoryConvertException(e);
    } catch (JsonMappingException e) {
      throw new JsonFactoryConvertException(e);
    } catch (IOException e) {
      throw new JsonFactoryConvertException(e);
    }
  }

  /**
   * Create a {@link JsonNode} from the last added object via
   * {@link #convert(Object)}. Any objects set for specific paths via
   * {@link #convert(String, Object)} will be ignored.
   * 
   * @return The {@link JsonNode}.
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws IOException
   */
  private JsonNode createSimpleObjectNode() throws JsonMappingException, JsonProcessingException, IOException {
    addMixins(mapper, mixins);
    String content = this.mapper.writeValueAsString(this.lastAddedObject);
    return this.mapper.readTree(content);
  }

  /**
   * Create a {@link JsonNode} and puts a new node for every given path via
   * {@link #convert(String, Object)}. Any object set without path
   * {@link #convert(Object)} will be ignored.
   * 
   * @return The {@link JsonNode}.
   * @throws JsonGenerationException
   * @throws JsonMappingException
   * @throws IOException
   */
  private JsonNode createPathObjectNode() throws JsonGenerationException, JsonMappingException, IOException {
    ObjectNode root = this.mapper.createObjectNode();
    Iterator<Map.Entry<String, Object>> iter = this.content.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, Object> path = iter.next();
      ObjectMapper mapper = new ObjectMapper();
      if (pathMixins.containsKey(path.getKey())) {
        addMixins(mapper, pathMixins.get(path.getKey()));
      }
      String content = mapper.writeValueAsString(path.getValue());
      root.put(path.getKey(), mapper.readTree(content));
    }
    return root;
  }

  /**
   * Adds {@code mixins} to the {@link ObjectMapper}.
   * 
   * @param mapper
   *          The {@link ObjectMapper} to set.
   * @param mixins
   *          The {@code mixins} to set.
   */
  private void addMixins(ObjectMapper mapper, Map<Class<?>, Class<?>> mixins) {
    for (Class<?> c : mixins.keySet()) {
      mapper.getSerializationConfig().addMixInAnnotations(c, mixins.get(c));
    }
  }
}
