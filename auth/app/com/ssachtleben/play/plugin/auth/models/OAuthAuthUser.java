package com.ssachtleben.play.plugin.auth.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.scribe.model.Token;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssachtleben.play.plugin.auth.providers.BaseOAuthProvider;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The abstract {@link OAuthAuthUser} contains {@code id} and {@code token}
 * properties.
 * <p>
 * All oauth provider uses this model which extend from this and pass it to
 * {@link AuthService}.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see BaseOAuthProvider
 */
@SuppressWarnings("serial")
public abstract class OAuthAuthUser extends AuthUser {

  /**
   * Keeps the id used during authentication process.
   */
  private String id;

  /**
   * Keeps the token used during authentication process.
   */
  private Token token;

  /**
   * Keeps the expire date if the token contains it.
   */
  private Long expires;

  /**
   * Default constructor for {@link OAuthAuthUser}.
   * 
   * @param id
   *          The id to set
   * @param token
   *          The token to set
   * @param data
   *          The data to set
   */
  public OAuthAuthUser(final String id, final Token token, final JsonNode data) {
    this.id = id;
    this.token = token;
    this.data = data;
    final Matcher matcher = Pattern.compile("expires=([^&]+)").matcher(token.getRawResponse());
    if (matcher.find()) {
      final String expires = matcher.group(1);
      if (NumberUtils.isNumber(expires)) {
        this.expires = Long.parseLong(expires);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.auth.models.Identity#id()
   */
  @Override
  public String id() {
    return id;
  }

  /**
   * @return The access {@link Token} for further requests.
   */
  public Token token() {
    return token;
  }

  /**
   * @return The expire time.
   */
  public Long expires() {
    return expires;
  }
}
