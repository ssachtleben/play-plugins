package com.ssachtleben.play.plugin.auth.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.builders.LinkedIn2Api;
import com.ssachtleben.play.plugin.auth.exceptions.AuthenticationException;
import com.ssachtleben.play.plugin.auth.models.LinkedInAuthUser;

/**
 * Provides authentication with LinkedIn oauth interface.
 * 
 * @author Sebastian Sachtleben
 */
@Provider(type = LinkedInAuthUser.class)
public class LinkedIn extends BaseOAuth2Provider<LinkedInAuthUser> {

  /**
   * The unique provider name for {@link LinkedIn} provider.
   */
  public static final String KEY = "linkedin";

  /**
   * The resource url.
   */
  public static final String RESOURCE_URL = "https://api.linkedin.com/v1/people";

  /**
   * Provides setting keys for {@link Facebook} provider.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class SettingKeys {

    /**
     * The fields fetched from api during authentication.
     */
    public static final String FIELDS = "fields";

  }

  /**
   * Provides default values for settings keys of {@link LinkedIn} provider.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class SettingDefault {

    /**
     * The default scope for {@link LinkedIn} oauth provider.
     */
    public static final String SCOPE = "r_basicprofile r_emailaddress";

    /**
     * The default fields for {@link LinkedIn} oauth provider.
     */
    public static final String FIELDS = "id,first-name,last-name,picture-url,email-address";

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
    return LinkedIn2Api.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.ssachtleben.play.plugin.auth.providers.BaseOAuthProvider#defaultScope()
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
  protected LinkedInAuthUser transform(Token token) throws AuthenticationException {
    String fields = config().getString(SettingKeys.FIELDS, SettingDefault.FIELDS);
    JsonNode me = data(token, String.format("%s/~:(%s)", RESOURCE_URL, fields));
    if (me == null || me.get("id") == null) {
      throw new AuthenticationException("Resource fields should at least contain the person id to identify the identity");
    }
    logger().info(String.format("Retrieved: %s", me));
    return new LinkedInAuthUser(token, me);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.ssachtleben.play.plugin.auth.providers.BaseOAuthProvider#beforeSignRequest
   * (org.scribe.model.OAuthRequest)
   */
  @Override
  public void beforeSignRequest(OAuthRequest request) {
    // Force linkin api to use json format
    request.addHeader("Content-Type", "application/json");
    request.addHeader("x-li-format", "json");
  }

}
