package com.ssachtleben.play.plugin.auth.builders;

/**
 * Provides linkedIn oauth2 access.
 * 
 * @author Sebastian Sachtleben
 */
public class LinkedIn2Api extends BaseOAuth2 {

  @Override
  public String authorizeEndpoint() {
    return "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&state=DCEEFWF45453sdffef424";
  }

  @Override
  public String accessTokenEndpoint() {
    return "https://www.linkedin.com/uas/oauth2/accessToken";
  }

  @Override
  public String tokenPattern() {
    return "\"access_token\":\"([^&\"]+)\"";
  }

  @Override
  public String getAccessTokenQueryName() {
    return "oauth2_access_token";
  }

}
