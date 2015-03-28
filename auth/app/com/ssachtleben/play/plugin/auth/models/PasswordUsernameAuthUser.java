package com.ssachtleben.play.plugin.auth.models;

import com.ssachtleben.play.plugin.auth.providers.PasswordEmail;
import com.ssachtleben.play.plugin.auth.providers.PasswordUsername;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link PasswordUsernameAuthUser} contains {@code username} and
 * {@code clearPassword} properties and is used by the {@link PasswordEmail}
 * provider and will be passed to {@link AuthService}.
 * 
 * @author Sebastian Sachtleben
 * @see AuthUser
 * @see PasswordEmail
 */
@SuppressWarnings("serial")
public class PasswordUsernameAuthUser extends PasswordAuthUser {

  /**
   * Keeps the username used during authentication process.
   */
  private String username;

  /**
   * Default constructor for {@link PasswordUsernameAuthUser}.
   * 
   * @param username
   *          The username to set
   * @param clearPassword
   *          The clearPassword to set
   */
  public PasswordUsernameAuthUser(final String username, final String clearPassword) {
    super(clearPassword);
    this.username = username;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.auth.models.Identity#provider()
   */
  @Override
  public String provider() {
    return PasswordUsername.KEY;
  }

  /**
   * @return the username
   */
  public String username() {
    return username;
  }
}
