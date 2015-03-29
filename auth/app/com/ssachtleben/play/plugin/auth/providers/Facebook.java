package com.ssachtleben.play.plugin.auth.providers;

import org.apache.commons.lang3.StringUtils;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.exceptions.AuthenticationException;
import com.ssachtleben.play.plugin.auth.models.FacebookAuthUser;

/**
 * Provides authentication with Facebook oauth2 interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = FacebookAuthUser.class)
public class Facebook extends BaseOAuth2Provider<FacebookAuthUser> {

  /**
   * The unique provider name for {@link Facebook} provider.
   */
  public static final String KEY = "facebook";

  /**
   * The Facebook Graph url.
   */
  public static final String RESOURCE_URL = "https://graph.facebook.com";

  /**
   * Provides setting keys for {@link Facebook} provider.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class SettingKeys {

    /**
     * The fields fetched from graph during authentication.
     */
    public static final String FIELDS = "fields";

  }

  /**
   * Provides default values for settings keys of {@link Facebook} provider.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class SettingDefault {

    /**
     * The default scope for {@link Facebook} oauth provider.
     */
    public static final String SCOPE = "email";

    /**
     * The default fields for {@link Facebook} oauth provider.
     */
    public static final String FIELDS = "id,name,email";

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.auth.providers.BaseProvider#key()
   */
  @Override
  public String key() {
    return KEY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.auth.providers.OAuthProvider#provider()
   */
  @Override
  public Class<? extends Api> provider() {
    return FacebookApi.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.ssachtleben.play.plugin.auth.providers.OAuthProvider#defaultScope()
   */
  @Override
  protected String defaultScope() {
    return SettingDefault.SCOPE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.ssachtleben.play.plugin.auth.providers.OAuthProvider#transform(org.
   * scribe.model.Token)
   */
  @Override
  protected FacebookAuthUser transform(Token token) throws AuthenticationException {
    JsonNode data = null;
    String fields = config().getString(SettingKeys.FIELDS, SettingDefault.FIELDS);
    if (StringUtils.isNotBlank(fields)) {
      if (fields == null || !"".equals(fields)) {
        data = me(token, fields);
      } else {
        data = new ObjectMapper().createObjectNode();
      }
    }
    logger().info("Retrieved: " + data.toString());
    return new FacebookAuthUser(token, data);
  }

  /**
   * Fetch data from Facebook Graph with {@link Token} and comma seperated list
   * of properties in {@code fields}.
   * 
   * @param token
   *          The {@link Token} to set.
   * @param fields
   *          The fields to set
   * @return User data as {@link JsonNode} fetched from Facebook Graph.
   * @throws AuthenticationException
   */
  private JsonNode me(Token token, String fields) throws AuthenticationException {
    return data(token, String.format("%s/me?fields=%s", RESOURCE_URL, fields));
  }
}
