package com.ssachtleben.play.plugin.auth.builders;

/**
 * Provides google oauth2 access.
 * 
 * @author Sebastian Sachtleben
 */
public class Google2Api extends BaseOAuth2 {

  @Override
  public String authorizeEndpoint() {
    return "https://accounts.google.com/o/oauth2/auth?response_type=code";
  }

  @Override
  public String accessTokenEndpoint() {
    return "https://accounts.google.com/o/oauth2/token";
  }

  @Override
  public String tokenPattern() {
    return "\"access_token\" : \"([^&\"]+)\"";
  }

}
