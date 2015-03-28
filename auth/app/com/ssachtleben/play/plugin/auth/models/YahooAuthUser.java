package com.ssachtleben.play.plugin.auth.models;

import com.fasterxml.jackson.databind.JsonNode;
import org.scribe.model.Token;

import com.ssachtleben.play.plugin.auth.providers.Facebook;
import com.ssachtleben.play.plugin.auth.providers.Yahoo;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link YahooAuthUser} contains {@code id}, {@code token} and {@code data}
 * properties. The data {@link JsonNode} contains all fetched information based
 * on choosen scope.
 * <p>
 * The {@link Yahoo} provider uses this model and pass it to {@link AuthService}
 * .
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see OAuthAuthUser
 * @see Facebook
 */
@SuppressWarnings("serial")
public class YahooAuthUser extends OAuthAuthUser {

  /**
   * Default constructor for {@link YahooAuthUser}.
   * 
   * @param id
   *          The id to set
   * @param token
   *          The token to set
   * @param data
   *          The data to set
   */
  public YahooAuthUser(String id, Token token, JsonNode data) {
    super(id, token, data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
   */
  @Override
  public String provider() {
    return Yahoo.KEY;
  }
}
