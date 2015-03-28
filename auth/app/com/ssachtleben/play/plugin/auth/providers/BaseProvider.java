package com.ssachtleben.play.plugin.auth.providers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import play.Configuration;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;

import com.ssachtleben.play.plugin.auth.Auth;
import com.ssachtleben.play.plugin.auth.Providers;
import com.ssachtleben.play.plugin.auth.exceptions.AuthenticationException;
import com.ssachtleben.play.plugin.auth.exceptions.MissingConfigurationException;
import com.ssachtleben.play.plugin.auth.models.AuthUser;
import com.ssachtleben.play.plugin.auth.models.Identity;
import com.ssachtleben.play.plugin.event.Events;

/**
 * Provides basic provider functionality.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseProvider<U extends Identity> {

  /**
   * Contains all setting keys provided by application.conf.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class SettingKeys {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
  }

  /**
   * Contains all event keys to execute custom code during authentication.
   * 
   * @author Sebastian Sachtleben
   */
  public static abstract class EventKeys {

    /**
     * Fired sync event before authentication process starts. Parameters are
     * provider string and context.
     */
    public static final String AUTHENTICATION_BEFORE = "authenticationBefore";

    /**
     * Fired async event after successful authentication. Parameters are
     * provider string and auth user.
     */
    public static final String AUTHENTICATION_SUCCESSFUL = "authenticationSuccessful";

    /**
     * Fired async event after failed authentication. Parameters are provider
     * string and context.
     */
    public static final String AUTHENTICATION_FAILED = "authenticationFailed";
  }

  /**
   * Provides access to settings from application.conf.
   * 
   * @return The provider {@link Configuration}.
   */
  protected Configuration config() {
    final Configuration config = Auth.config();
    if (config != null) {
      return config.getConfig(key());
    }
    return null;
  }

  /**
   * Returns logger from provider instance.
   * 
   * @return The {@link Logger.ALogger} instance.
   */
  protected Logger.ALogger logger() {
    return Logger.of(getClass());
  }

  /**
   * Handles authenticate process for this {@link BaseProvider}.
   * 
   * @param ctx
   *          The {@link Context} to set.
   * @return Play {@link Result} object.
   * @throws AuthenticationException
   */
  public Result login(final Context ctx) throws AuthenticationException {
    Events.instance().publish(EventKeys.AUTHENTICATION_BEFORE, key(), ctx);
    AuthUser authUser = handle(ctx);
    if (authUser != null) {
      Object user = null;
      Method authMethod = Providers.getMethod(key());
      if (authMethod != null) {
        logger().info(String.format("Using custom auth method for %s provider", key()));
        try {
          user = authMethod.invoke(null, ctx, authUser);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          logger().error(String.format("Failed to invoke authenticate method for %s provider", key()), e);
        }
      } else {
        logger().info("Using default auth service ... (maybe deprecated soon)");
        user = Auth.service().find(authUser);
      }
      logger().debug("User: " + user);
      if (user != null) {
        ctx.session().put(Auth.SESSION_USER_KEY, user.toString());
        Events.instance().publishAsync(EventKeys.AUTHENTICATION_SUCCESSFUL, key(), user);
        return onSuccess(ctx);
      }
    }
    Events.instance().publishAsync(EventKeys.AUTHENTICATION_FAILED, key(), ctx);
    return onError(ctx);
  }

  /**
   * Handles success behavior for this provider.
   * 
   * @param context
   *          The current {@link Context}.
   * @return Play {@link Result} object.
   */
  protected Result onSuccess(final Context context) {
    return handleRedirect(SettingKeys.SUCCESS, Results.ok());
  }

  /**
   * Handles error behavior for this provider.
   * 
   * @param context
   *          The current {@link Context}.
   * @return Play {@link Result} object.
   */
  protected Result onError(final Context context) {
    return handleRedirect(SettingKeys.ERROR, Results.badRequest());
  }

  /**
   * Handles redirect to url.
   * 
   * @param url
   *          The url to set.
   * @return Play {@link Result} object.
   */
  protected Result handleRedirect(final String settingKey, final Result result) {
    final Configuration config = config();
    final Set<String> configKeys = config != null ? config.keys() : new HashSet<String>();
    if (configKeys.size() > 0 && configKeys.contains(settingKey)) {
      return Results.redirect(config.getString(settingKey));
    }
    return result;
  }

  /**
   * Provides the unqiue key for this {@link BaseProvider}.
   * 
   * @return The unqiue key.
   */
  public abstract String key();

  /**
   * Handles login with this provider.
   * 
   * @param context
   *          The current {@link Context}.
   * @return The {@link AuthUser}.
   * @throws AuthenticationException
   */
  protected abstract AuthUser handle(final Context context) throws AuthenticationException;

  /**
   * List of setting keys which must be provided from the application.conf.
   * These keys will be validated during application start.
   * 
   * @return List of setting keys.
   */
  protected List<String> settingKeys() {
    return Collections.<String> emptyList();
  }

  /**
   * Validates {@link BaseProvider} registered as plugin during application
   * start and checks if all mandatory setting keys provided by
   * {@link #settingKeys()} are properly set in application.conf.
   * 
   * @throws MissingConfigurationException
   *           The exception throws for missing setting keys in
   *           application.conf.
   */
  public void validate() throws MissingConfigurationException {
    final List<String> settingKeys = settingKeys();
    if (settingKeys != null && settingKeys.size() >= 0) {
      final Configuration config = config();
      if (config == null && settingKeys.size() > 0) {
        throw new MissingConfigurationException(String.format("Creating %s provider failed due missing conf '%s.%s'", WordUtils.capitalize(key()),
            Auth.SettingKeys.AUTH, key()));
      }
      Iterator<String> iter = settingKeys.iterator();
      while (iter.hasNext()) {
        String key = iter.next();
        String value = config.getString(key);
        if (StringUtils.isEmpty(value)) {
          throw new MissingConfigurationException(String.format("Creating %s provider failed due missing conf settings key '%s.%s.%s'",
              WordUtils.capitalize(key()), Auth.SettingKeys.AUTH, key(), key));
        }
      }

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("%s Provider", WordUtils.capitalize(key()));
  }
}